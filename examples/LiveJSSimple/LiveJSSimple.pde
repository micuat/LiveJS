/**
 * LiveJSSimple by Naoto Hieda
 */

import LiveJS.*;

LiveJS js;

void setup() {
  size(800, 800, P3D);
  frameRate(60);
  
  js = new LiveJS(this, dataPath("example.js"));
}


void draw() {
  js.draw();
}