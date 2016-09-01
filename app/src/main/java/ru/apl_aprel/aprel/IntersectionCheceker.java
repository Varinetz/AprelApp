package ru.apl_aprel.aprel;

/**
 * Created by Руслан on 01.09.2016.
 */
public class IntersectionCheceker {

    public final Segment segment1, segment2;
    private Boolean hasIntersection;

    public IntersectionCheceker(Segment segment1, Segment segment2){
        this.segment1 = segment1;
        this.segment2 = segment2;
    }

    public IntersectionCheceker(double x1, double y1, double x2, double y2,
                                double x3, double y3, double x4, double y4){
        Point start1 = new Point(x1, y1);
        Point end1 = new Point(x2, y2);
        Point start2 = new Point(x3, y3);
        Point end2 = new Point(x4, y4);
        this.segment1 = new Segment(start1, end1);
        this.segment2 = new Segment(start2, end2);
    }

    public boolean hasIntersection(){
        if (hasIntersection != null)
            return hasIntersection;

        if (segment1.isVertical){
            if ( (segment2.start.x - segment1.start.x)*(segment2.end.x - segment1.start.x) > 0 )
                hasIntersection = false;
            else {
                double fx_at_segment1startx = segment1.slope * segment1.start.x + segment1.intercept;
                double smaller, larger;
                if (segment1.start.x < segment1.end.x ){
                    smaller = segment1.start.x;larger = segment1.end.x;
                }
                else {
                    larger = segment1.start.x;smaller = segment1.end.x;
                }
                if (smaller <= fx_at_segment1startx && fx_at_segment1startx <= larger)
                    hasIntersection = true;
                else
                    hasIntersection = false;
            }
        }
        else if (segment2.isVertical){
            hasIntersection = new IntersectionCheceker(segment2, segment1).hasIntersection();
        }
        else { //both segment1 and segment2 are not vertical
            if (segment1.slope == segment2.slope)
                hasIntersection = false;
            else {
                double x1 = segment1.start.x;
                double y1 = segment1.start.y;
                double x2 = segment1.end.x;
                double y2 = segment1.end.y;
                double x3 = segment2.start.x;
                double y3 = segment2.start.y;
                double x4 = segment2.end.x;
                double y4 = segment2.end.y;
                double x = ((x4*y3-y4*x3)/(x4-x3) - (x2*y1-y2*x1)/(x2-x1))
                        /( (y2-y1)/(x2-x1) - (y4-y3)/(x4-x3));

                double smaller, larger;
                if (x1 < x2){
                    smaller = x1; larger = x2;
                }
                else {
                    smaller = x2; larger = x1;
                }
                if (smaller <= x && x <= larger)
                    hasIntersection = true;
                else
                    hasIntersection = false;
            }
        }
        return hasIntersection;
    }


    public static void main(String[] args) {

        IntersectionCheceker checker1 = new IntersectionCheceker(1, 0, 1.2, 3, 2.4, 5, 3.6, -1);
        boolean hi1 = checker1.hasIntersection();
        System.out.println("IntersectionCheceker.main() 64 hi1 = " + hi1);
    }
}