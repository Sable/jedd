#!/usr/bin/python

import sys, os
import cgi, cgitb
import sqlite
import string

def series(column, name, offset):
    return """ "<(echo -1000 -1000; sqlite -column profile.db \\"select level, nodes from   events join shapes on shapes.eventid = events.%s  where  events.id = %s\\")" using ($1+0.%s):2 title "%s" with impulses %s, "<(echo -1000 -1000; sqlite -column profile.db \\"select level, nodes from   events join shapes on shapes.eventid = events.%s  where  events.id = %s\\")" using ($1+0.%s):2 title "%s" with points %s """ % ( column, form["event"].value, offset, name, offset, column, form["event"].value, offset, name, offset )
#    return """ "<(echo -1000 -1000; sqlite -column profile.db \\"select level, nodes from   events join shapes on shapes.eventid = events.%s  where  events.id = %s\\")" using ($1+0.%s):2 title "%s" with impulses %s """ % ( column, form["event"].value, offset, name, offset )



cgitb.enable()

form = cgi.FieldStorage()

print "Content-Type: image/png\r\n"

os.putenv("GNUTERM", "png")
(pin, pout) = os.popen2("gnuplot")
print >>pin, "set term png"
print >>pin, "set xrange [-0.5:`sqlite -column profile.db \"select max(maxpos) from physdoms\"`+0.5]"
print >>pin, "set key below noautotitles "
print >>pin, "set xlabel 'Level'"
print >>pin, "set ylabel 'Nodes'"
print >>pin, """set title "`sqlite -column profile.db \"select type, shrt from events join stacks on events.stackid = stacks.id where events.id = %s\"`" """ % form["event"].value

plotcmd = "plot %s, %s, %s" % (
    series("inputA", "Input A", "1"),
    series("inputB", "Input B", "2"),
    series("output", "Output", "3")
)


conn = sqlite.connect(db="profile.db", mode=077)
cursor = conn.cursor()

order = "total desc"
if "order" in form:
    order = form["order"].value;

cursor.execute("""
    select name, minpos, maxpos from physdoms
""")
for row in cursor.fetchall():
    shortname = string.split(row.name,".")[-1]
    vertpos = row.minpos %5;
    print >>pin, """set label "%s" at first %s, graph %s center""" % ( shortname, (row.minpos+row.maxpos)/2, .99-vertpos*.025 )
    print >>pin, """set arrow heads size graph .01,90 from first %s, graph %s to first %s, graph %s""" % ( row.minpos, .975-vertpos*.025, row.maxpos, .975-vertpos*.025 )
conn.close()

print >>pin, plotcmd

pin.close()
graph = pout.read()
print graph
file = open("/tmp/graph.png","w")
print >>file, graph
file.close()
#print plotcmd


