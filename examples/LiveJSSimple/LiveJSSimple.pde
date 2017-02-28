/**
 * LiveJSSimple by Naoto Hieda
 */

import LiveJS.*;

LiveJS js;

void setup() {
  size(400,400);
  frameRate(60);
  
  js = new LiveJS(this, dataPath("example.js"));
}


void draw() {
  js.draw();
}