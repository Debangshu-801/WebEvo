# WebEvo
Implementation of the paper WEBEVO: Taming Web Application Evolution via Semantic Change Detection.
## Introduction
In this work, we combined DOM-tree based comparison of old and new version of a web page, non-content change detection module by leveraging semantic, and visual information which identifies relevant structural changes together to monitor changes in a website.
![Overview of Workflow of WebEvo](Overview.png)
## Requirements
+ JAVA Version: 1.8
## Usage
The major modules of WebEvo are listed below:

1. DOM Structure Comparison.

We use the old and new versions of a web page as inputs to our DOM-tree Based Changed Element Detection module to detect changes on a web page based on DOM tree structures. 

+ Input.

Old and new version of the web page.

+ Output.

Changes in the DOM tree structure.
  
To run the jar file:
```bash
java -jar api-monitor-0.0.1-SNAPSHOT-jar-with-dependencies.jar -oldpage: <old version of the web page> -newpage: <new version of the web page>
```

api-monitor-0.0.1-SNAPSHOT-jar-with-dependencies.jar is in [Dom Structure Comparison](DomStructureComparison).

2. Non-content Change Detection.

The Non-content change detection module based on HTML file histories is at the core of our proposed framework. The goal of this module is to prune the detected changes from the previous step to find only Non-content changes.

+ Input.

Old version of the web page, and three html file histories.

+ Output.

XPaths labeled as dynamic or static.
  
To run the jar file:
```bash
java -jar api-monitor-0.0.1-SNAPSHOT-jar-with-dependencies.jar -oldpage: <old version of the web page> -historypage1: <history1> -historypage2: <history3> -historypage3: <history3>
```

api-monitor-0.0.1-SNAPSHOT-jar-with-dependencies.jar is in [Non-content Change Detection](Non-contentChangeDetection).
  
3. Graphic Image Analysis.

This module detects the position and structure of different graphical elements through the use of optical character recognition (OCR), edge detection, and contour processing techniques to generate graphical areas that correspond to pictures or text areas on the web page. Then it is used to extract the visual hierarchical relationship among the web page layout elements. The source code is in [graphic-image-analysis](graphic-image-analysis).

## Acknowledgement
[Vista](https://github.com/saltlab/vista)  
