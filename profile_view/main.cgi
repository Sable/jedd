#!/usr/bin/python
import sqlite
import cgi, cgitb
import urllib
import string

cgitb.enable()

def set_order(order):
    global form
    new_form = {}
    for k in form.keys():
        new_form[k] = form[k].value
    new_form["order"] = order
    return urllib.urlencode(new_form);

def print_overview(form):
    conn = sqlite.connect(db="profile.db", mode=077)
    cursor = conn.cursor()

    order = "total desc"
    if "order" in form:
        order = form["order"].value;

    cursor.execute("""
        select stackid, shrt, type, count(*) as count, sum(time) as total, max(sizesA.nodes) as inA, max(sizesB.nodes) as inB, max(sizesout.nodes) as out
        from events
             join stacks on stacks.id = events.stackid
             join sizes as sizesA on inputA = sizesA.eventid
             join sizes as sizesB on inputB = sizesB.eventid
             join sizes as sizesout on output = sizesout.eventid
        group by events.stackid
        order by %s
    """ % order)

    print """<table>"""

    print """
            <tr bgcolor="lightblue">
            <td><a href="main.cgi?%s">Type</a></td>
            <td><a href="main.cgi?%s">Count</td>
            <td><a href="main.cgi?%s">Time</td>
            <td><a href="main.cgi?%s">Input A</td>
            <td><a href="main.cgi?%s">Input B</td>
            <td><a href="main.cgi?%s">Output</td>
            <td><a href="main.cgi?%s">Location</a></td>
            </tr>
        """ % ( 
            set_order("type"),
            set_order("count desc"),
            set_order("total desc" ),
            set_order("ina desc"), set_order("inb desc"), set_order("out desc"),
            set_order("shrt") )
    for row in cursor.fetchall():
        print """
            <tr bgcolor="lightgray">
            <td>%s</td>
            <td align="right">%s</td>
            <td align="right">%s&nbsp;ms</td>
            <td align="right">%s</td>
            <td align="right">%s</td>
            <td align="right">%s</td>
            <td><a href="main.cgi?stackid=%s">%s</a></td>
            </tr>
        """ % ( 
        row.type, row.count, row.total, row.ina, row.inb, row.out,
        row.stackid, row.shrt
        )

    print "</table>"

    conn.close()

def print_detail(form):
    conn = sqlite.connect(db="profile.db", mode=077)

    order = "time desc"
    if "order" in form:
        order = form["order"].value;

    cursor = conn.cursor()
    cursor.execute("""
        select stack
        from stacks
        where id=%s
    """, form["stackid"].value )

    print """<a href="main.cgi">Back to main profile</a><br>"""
    for row in cursor.fetchall():
        print """Stack trace is: %s\n<br>""" % string.replace(row.stack, "\n", "<br>")

    cursor = conn.cursor()
    cursor.execute("""
        select events.id as eventid, type,time,sizesA.nodes as ina, sizesB.nodes as inb, sizesout.nodes as out
        from events
             join sizes as sizesA on inputA = sizesA.eventid
             join sizes as sizesB on inputB = sizesB.eventid
             join sizes as sizesout on output = sizesout.eventid
        where stackid=%s
        order by %s 
    """ % ("%s", order), form["stackid"].value )

    print """<table>"""

    print """
            <tr bgcolor="lightblue">
            <td><a href="main.cgi?%s">Type</a></td>
            <td><a href="main.cgi?%s">Time</td>
            <td><a href="main.cgi?%s">Input A</td>
            <td><a href="main.cgi?%s">Input B</td>
            <td><a href="main.cgi?%s">Output</td>
            </tr>
        """ % ( set_order("type"),
            set_order("time desc"),
            set_order("ina desc"), set_order("inb desc"), set_order("out desc") )
    for row in cursor.fetchall():
        print """
        <tr bgcolor="lightgray">
        <td><a href="graph.cgi?event=%s">%s</a></td>
        <td align="right">%s&nbsp;ms</td>
        <td align="right">%s</td>
        <td align="right">%s</td>
        <td align="right">%s</td>
        </tr>
        """ % ( row.eventid, row.type, row.time, row.ina, row.inb, row.out )

    print "</table>"

    conn.close()


print "Content-Type: text/html\n\n"


form = cgi.FieldStorage()

if "stackid" in form:
    print_detail(form)
else:
    print_overview(form)



