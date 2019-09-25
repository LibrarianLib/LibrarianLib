import bpy
from math import floor, ceil
from collections import namedtuple

Sample = namedtuple('Sample', 'frame value')

class EncodedAction:
    def __init__(self, action):
        self.name = action.name
        self.curves = [Curve(fcurve).optimize() for fcurve in action.fcurves]

class Curve:
    def __init__(self, fcurve=None):
        self.path = ""
        self.index = 0
        self.samples = []
        if fcurve:
            frange = (int(floor(fcurve.range()[0])), int(ceil(fcurve.range()[1])))

            self.path = fcurve.data_path
            self.index = fcurve.array_index
            self.samples = [Sample(frame, fcurve.evaluate(frame)) for frame in range(frange[0], frange[1] + 1)]

    def optimize(self):
        """Create an optimized copy of this curve"""
        optimized = Curve()
        optimized.path = self.path
        optimized.index = self.index

        samples = self.samples
        if len(samples) < 3: 
            # it's impossible to further optimize one or two samples and the optimization 
            # code is much simpler if we can assume there are at least three samples
            return optimized

        value_range = (min([frame[1] for frame in samples]), max([frame[1] for frame in samples]))
        error_tolerance = (value_range[1] - value_range[0]) * 0.01

        def maximum_error(start_index, end_index):
            start = samples[start_index]
            end = samples[end_index]
            slope = (end.value - start.value) / (end.frame - start.frame)

            # calculate the difference between the value that would be expected when lerping and the actual value
            def error(sample):
                expected = start.value + (sample.frame - start.frame) * slope
                return sample.value - expected

            return max([abs(error(samples[i])) for i in range(start_index + 1, end_index)])

        latest_sample = 0
        
        if maximum_error(0, len(samples) - 1) < 1e-5:
            optimized.samples.append(samples[0])
            optimized.samples.append(samples[-1])
            return optimized

        # graphical explanation: https://i.imgur.com/afUuorb.png
        # the first value will always be present
        optimized.samples.append(samples[0])
        # skip to testing the third value. The error between the first and second values will always be 0, since there are no points between them
        for i in range(2, len(samples)):
            current_error = maximum_error(latest_sample, i)
            # second section of condition tests to see if this is a deviation after a long sequence of linear points. It does this by
            # checking if the entire section since the latest sample up to, but not including, the current one have a tiny error. If they do,
            # and this one doesn't, then it assumes it's at the end of a linear section and adds a keyframe accordingly.
            if current_error > error_tolerance or (current_error > 1e-5 and i - latest_sample > 2 and maximum_error(latest_sample, i-1) < 1e-5):
                # this point introduces too much of an error, so we add the previous point
                optimized.samples.append(samples[i-1])
                latest_sample = i-1
        # and we only ever add the "previous" value so the loop will never add the last value, which should always be present
        optimized.samples.append(samples[-1])

        return optimized
