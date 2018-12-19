#!/usr/bin/env python
from __future__ import print_function

import os
import argparse
import itertools
import functools
import operator

# Fine the default input file relative to this file; this allows the program to
# be invoked from any working directory
mydir = os.path.abspath(os.path.dirname(__file__))
default_input = os.path.join(mydir, "../dat/day/1/input.txt")

# Create a command line parser
parser = argparse.ArgumentParser()
parser.add_argument(
    "input",
    type=str,
    nargs="?",
    help="the input file to use",
    default=default_input
)
args = parser.parse_args()

##############################################################################
# The straight forward solution; calculated in two-passes
##############################################################################

with open(args.input) as f:
    # Load all the numers into the file.  WARNING if the input
    # file is large we could run out of memory
    nums = [ int(x) for x in f ]

    # The first answer is easy to obtain with sum()
    ans1 = sum(nums)

 
    # The second answer uses a set() and cycle() 
    ans2 = None
    seen = set()

    accum = 0
    while ans2 == None:
        for x in itertools.cycle(nums):
            accum += x
            if accum in seen:
                ans2 = accum
                break
            else:
                seen.add(accum)
    print("Answer:", ans1, ans2)


##############################################################################
# Let's implement an efficent method of cycling through the file without having
# to load all of the contents in memory.
##############################################################################

def cycle_file(f, include_last=False):
    """
    Cycle through the lines in the file, behaves like:

        >>> cycle_file(f)

    But doesn't require auxiliary storage for all of the lines so it's safe to
    use on large files.

    If f is empty, then cycle_file() returns immediately.

    If include_last is True, then when the EOF is reached the empty string will
    be yielded.  This allows the detection of when the cycle repeats
    """

    line = f.readline()
    if line == '':
        return

    while True:
        yield line
        line = f.readline()
        if line == '':
            f.seek(0)
            if include_last == False:
                line = f.readline()

def safe_int(v):
    """
    Safe conversion to int that returns None when the provided value cannot be
    coerced to an int.  Useful for list-comprehension/map/imap.
    """
    try:
        return int(v)
    except (ValueError, TypeError):
        return None


##############################################################################
# This solution calculates both answers in one pass, using cycle_file()
##############################################################################
with open(args.input) as f:
    ans1, ans2 = None, None
    seen = set()

    accum = 0
    for num in itertools.imap(safe_int, cycle_file(f, include_last=True)):
	if (num is not None):
	    accum += num
	    if (ans2 == None) and (accum in seen):
		ans2 = accum
	    else:
		seen.add(accum)
	elif (ans1 is None):
	    ans1 = accum

        if (ans1 is not None) and (ans2 is not None):
            break
        
    print("Answer:", ans1, ans2)


##############################################################################
# We can take this one-step further by making helper functions for an
# interruptable reduce.
##############################################################################
class Reduced(StopIteration):
    """
    Used within interruptible_reduce by the function to declare that it wants
    to end the reduce and return a value.
    """
    def __init__(self, result=None):
        self.result = result

def interruptible_reduce(func, *args):
    """
    Version of reduce that can be interrupted by throwing Reduced
    """
    try:
        return reduce(func, *args)
    except Reduced as r:
        return r.result

##############################################################################
# Now we can write a Solver
##############################################################################

with open(args.input) as f:

    def solver(accum, v):
        ans1, ans2, summation, seen = accum
        if v is not None:
            summation += v
	    if (ans2 == None) and (summation in seen):
                ans2 = summation
            else:
                seen.add(summation)
        elif (ans1 is None):
            ans1 = summation

        if (ans1 is not None) and (ans2 is not None):
            raise Reduced((ans1, ans2))
	else:
            return (ans1, ans2, summation, seen)

    ans1, ans2 = interruptible_reduce(
        solver,
        itertools.imap(safe_int, cycle_file(f, include_last=True)),
        (None, None, 0, set())
    )
    print("Answer:", ans1, ans2)
