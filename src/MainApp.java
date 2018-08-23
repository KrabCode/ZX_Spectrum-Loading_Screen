import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

public class MainApp extends PApplet{

    public static void main(String[] args) {
        PApplet.main("MainApp");
    }

    int white, red, cyan, blue, yellow, black;

    PImage img;
    int currentPhaseIndex = 0;
    int currentPhaseStartedMillis = 0;
    ArrayList<Phase> ps = new ArrayList<Phase>();

    PVector center;
    PVector centralTopLeftCorner;

    Phase thisPhase;
    float thisPhaseElapsedMillis;

    float centralW;
    float centralH;

    public void settings() {
        size(800,600);
    }

    public void setup() {
        rectMode(CENTER);
        colorMode(HSB);
        frameRate(30);
        white = color(200);
        red = color(0,255,255);
        cyan = color(140,255,255);
        blue = color(160,255,255);
        yellow = color(30, 255,255);
        black = color(0);

        center = new PVector(width/2, height/2);
        centralW = width-width/4;
        centralH = height-height/4;
        centralTopLeftCorner = new PVector(width/2-centralW/2, height/2-centralH/2);

//        img = loadImage("outrun.jpg");

        ps.add(new Phase(   PhaseType.BLACK_ON_WHITE_RED_LINES, 500));

        currentPhaseStartedMillis = millis();
    }

    public void draw() {
        if(currentPhaseIndex >= ps.size()) {
            noLoop();
            return;
        }
        thisPhase = ps.get(currentPhaseIndex);
        thisPhaseElapsedMillis = millis() - currentPhaseStartedMillis;
        if(thisPhaseElapsedMillis >= thisPhase.duration){
            currentPhaseIndex++;
            currentPhaseStartedMillis = millis();
//            println("index changed to " + currentPhaseIndex + " because " + thisPhaseElapsedMillis +">"+ thisPhase.duration);
        }
        drawCurrentPhase();
    }

    float elapsedNormalized(){
        return map(thisPhaseElapsedMillis, 0, thisPhase.duration, 0 ,1);
    }

    class Phase{
        float duration;
        PhaseType type;
        public Phase(PhaseType type, float duration){
            this.type = type;
            this.duration = duration;
        }
    }

    enum PhaseType{
        BLACK_ON_WHITE_RED_LINES,
        COPYRIGHT_1982_KRAB_RESEARCH_LTD,
        LOAD_COMMAND_LINE,
        WHITE,
        WHITE_ON_CYAN,
        WHITE_ON_CYAN_WITH_RED_STRIPES,
        WHITE_ON_BLUE_WITH_YELLOW_SHORT,
        TEXT_BYTES_WHITE,
        TEXT_BYTES_WHITE_ON_CYAN,
        TEXT_BYTES_WHITE_ON_CYAN_WITH_RED_STRIPES,
        BW_SEVENTHS_OF_THIRDS_ON_WHITE_ON_BLUE_WITH_YELLOW,
        COLOR_PASS,
        COLORED_PICTRURE_ON_GRAY
    }

    private void drawCurrentPhase() {
        switch (thisPhase.type){
            case BLACK_ON_WHITE_RED_LINES:{
                background(white);
                noStroke();
                fill(black);
                rectMode(CORNER);
                rect(centralTopLeftCorner.x, centralTopLeftCorner.y, centralW, centralH);
                float lineCount = 32;
                float lineDistance = centralW / lineCount;
                float offset = 2f;
                for(float x = center.x - centralW /2 + lineDistance/2; x < center.x + centralW/2; x+= lineDistance){
                    strokeWeight(2);
                    stroke(red);
                    line(x, center.y - centralH /2 + offset , x, center.y + centralH /2 - offset);
                }
                float covered = elapsedNormalized()*centralH;
                stroke(black);
                int coverStepCount = 10;
                int coveredThreshold = floor(map(covered, 0, centralH, 0, coverStepCount));

                if(coveredThreshold == coverStepCount) {
                    rectMode(CORNER);
                    noStroke();
                    rect(centralTopLeftCorner.x, centralTopLeftCorner.y, centralW, centralH);
                    break;
                }

                float x0 = centralTopLeftCorner.x;
                float y0 = centralTopLeftCorner.y;
                float x1 = centralTopLeftCorner.x+centralW;
                float y1 = centralTopLeftCorner.y+centralH/coverStepCount*coveredThreshold;

                for(float y = y0+lineDistance; y < y1; y+=lineCount){
                    float i = (y1 - y);
                    strokeCap(SQUARE);
                    strokeWeight(min(lineDistance*2,elapsedNormalized()*i/5f));
                    line(x0, y, x1, y);
                }
                break;
            }
        }
    }

}