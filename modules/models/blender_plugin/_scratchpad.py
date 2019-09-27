# macro commands to comment out incompatible type hints and copy the entire file
# %s/^\(\s*\w\+\)\s*:\s*[ a-zA-Z\[\],]\+\s*=/\1 =
# %s/^\s*\w\+:\s*[ a-zA-Z\[\],]\+$/#_&
# %y
