#!/usr/bin/python

import sys, os
import cgi, cgitb

cgitb.enable()


form = cgi.FieldStorage()

print "Content-Type: image/png\r\n"

(pin, pout) = os.popen2("gnuplot")
print >>pin, "set term png"
print >>pin, "set xrange [-0.5:`sqlite -column profile.db \"select max(level) from shapes\"`+0.5]"
print >>pin, "set xlabel 'Level'"
print >>pin, "set ylabel 'Nodes'"
print >>pin, """set title "`sqlite -column profile.db \"select type, shrt from events join stacks on events.stackid = stacks.id where events.id = %s\"`" """ % form["event"].value

def series(column, name, offset):
    return """ "<(echo -1000 -1000; sqlite -column profile.db \\"select level, nodes from   shapes join events on shapes.eventid = events.%s  where  events.id = %s\\")" using ($1+0.%s):2 title "%s" with impulses %s, "<(echo -1000 -1000; sqlite -column profile.db \\"select level, nodes from   shapes join events on shapes.eventid = events.%s  where  events.id = %s\\")" using ($1+0.%s):2 title "%s" with points %s """ % ( column, form["event"].value, offset, name, offset, column, form["event"].value, offset, name, offset )
plotcmd = "plot %s, %s, %s" % (
    series("inputA", "Input A", "1"),
    series("inputB", "Input B", "2"),
    series("output", "Output", "3")
)
print >>pin, plotcmd
pin.close()
print pout.read()
print plotcmd

