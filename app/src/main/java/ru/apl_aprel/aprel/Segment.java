package ru.apl_aprel.aprel;

/**
 * Created by Руслан on 01.09.2016.
 */
public class Segment {

    public final Point start, end;
    public final boolean isVertical;
    public final double slope, intercept;

    public Segment(Point start, Point end) {
        this.start = start;
        this.end = end;
        //set isVertical, which indicates whether this Line
        //is vertical or not on the coordinate plane
        if (start.x == end.x)
            isVertical = true;
        else
            isVertical = false;

        //set slope and intercept
        if (!isVertical){
            slope = (start.y - end.y) / (start.x - end.x);
            intercept = (end.x * start.y - start.x * end.y ) /(start.x - end.x);
        }
        else {
            slope = Double.MAX_VALUE;
            intercept = - Double.MAX_VALUE;
        }
    }
}
