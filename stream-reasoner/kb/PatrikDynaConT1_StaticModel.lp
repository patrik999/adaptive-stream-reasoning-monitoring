out(X,Y) :- mapLane(X), hasGeo(X,Y).

% Simple concept hierarchies

mapLane(X) :- mapLaneIn(X).
mapLane(X) :- mapLaneOut(X).

% Intersections 100 (Direction NW)
mapIntersection(i100).
hasGeo(i100,"POLYGON((430 140, 520 140, 520 220, 430 220, 430 140))").

% Intersections 200 (Direction NE)
mapIntersection(i200).
hasGeo(i200,"POLYGON((560 140, 655 140, 655 220, 560 220, 560 140))").

% Intersections 300 (Direction SW)
mapIntersection(i300).
hasGeo(i300,"POLYGON((430 35, 520 35, 520 120, 430 120, 430 35))").

% Intersections 400 (Direction SE)
mapIntersection(i400).
hasGeo(i400,"POLYGON((560 35, 660 35, 660 120, 560 120, 560 35))").


% Road Segments
roadSegment(rs100_200).
roadSegment(rs100_300).
roadSegment(rs200_400).
roadSegment(rs300_400).

% Signal Groups
mapSignalGroup(i100_sg1).
mapSignalGroup(i100_sg2).
mapSignalGroup(i100_sg3).
mapSignalGroup(i100_sg4).
mapSignalGroup(i200_sg1).
mapSignalGroup(i200_sg2).
mapSignalGroup(i200_sg3).
mapSignalGroup(i200_sg4).
mapSignalGroup(i300_sg1).
mapSignalGroup(i300_sg2).
mapSignalGroup(i300_sg3).
mapSignalGroup(i300_sg4).
mapSignalGroup(i400_sg1).
mapSignalGroup(i400_sg2).
mapSignalGroup(i400_sg3).
mapSignalGroup(i400_sg4).


%%//////////////////////////////
%% Intersection 100 (100)
%%//////////////////////////////

% Lanes and their Geometries
mapLaneIn(i100_l1).
mapLaneIn(i100_l2).
mapLaneOut(i100_l3).
mapLaneOut(i100_l4).
mapLaneIn(i100_l5).
mapLaneIn(i100_l6).
mapLaneOut(i100_l7).
mapLaneOut(i100_l8).
mapLaneIn(i100_l9).
mapLaneIn(i100_l10).
mapLaneOut(i100_l11).
mapLaneOut(i100_l12).
mapLaneIn(i100_l13).
mapLaneIn(i100_l14).
mapLaneOut(i100_l15).
mapLaneOut(i100_l16).

hasGeo(i100_l1,"POLYGON((441 168.5, 465 168.5, 465 172, 441 172, 441 168.5))").
hasGeo(i100_l2,"POLYGON((441 165, 465 165, 465 168.5, 441 168.5, 441 165))").
hasGeo(i100_l5,"POLYGON((477.5 162, 477.5 137, 480.7 137, 480.7 162, 477.5 162))").
hasGeo(i100_l6,"POLYGON((480.7 162, 480.7 140, 484 140, 484 162, 480.7 162))").
hasGeo(i100_l9,"POLYGON((490 172, 512 172, 512 175.7, 490 175.7, 490 172))").
hasGeo(i100_l10,"POLYGON((490 175.7, 512 175.7, 512 179, 490 179, 490 175.7))").
hasGeo(i100_l13,"POLYGON((474 187, 477.4 187, 477.4 212, 474 212, 474 187))").
hasGeo(i100_l14,"POLYGON((470 187, 474 187, 474 212, 470.4 212, 470 187))").


% Lane Connections
connected(i100_l1,i100_l3).
connected(i100_l1,i100_l4).
connected(i100_l1,i100_l7).
connected(i100_l1,i100_l8).
connected(i100_l1,i100_l11).
connected(i100_l1,i100_l12).

connected(i100_l2,i100_l3).
connected(i100_l2,i100_l4).
connected(i100_l2,i100_l7).
connected(i100_l2,i100_l8).
connected(i100_l2,i100_l11).
connected(i100_l2,i100_l12).

connected(i100_l5,i100_l7).
connected(i100_l5,i100_l8).
connected(i100_l5,i100_l11).
connected(i100_l5,i100_l12).
connected(i100_l5,i100_l15).
connected(i100_l5,i100_l16).

connected(i100_l6,i100_l7).
connected(i100_l6,i100_l8).
connected(i100_l6,i100_l11).
connected(i100_l6,i100_l12).
connected(i100_l6,i100_l15).
connected(i100_l6,i100_l16).

connected(i100_l9,i100_l3).
connected(i100_l9,i100_l4).
connected(i100_l9,i100_l11).
connected(i100_l9,i100_l12).
connected(i100_l9,i100_l15).
connected(i100_l9,i100_l16).

connected(i100_l10,i100_l3).
connected(i100_l10,i100_l4).
connected(i100_l10,i100_l11).
connected(i100_l10,i100_l12).
connected(i100_l10,i100_l15).
connected(i100_l10,i100_l16).

connected(i100_l13,i100_l3).
connected(i100_l13,i100_l4).
connected(i100_l13,i100_l7).
connected(i100_l13,i100_l8).
connected(i100_l13,i100_l15).
connected(i100_l13,i100_l16).

connected(i100_l14,i100_l3).
connected(i100_l14,i100_l4).
connected(i100_l14,i100_l7).
connected(i100_l14,i100_l8).
connected(i100_l14,i100_l15).
connected(i100_l14,i100_l16).

% Lane part of Intersection
isPartOf(i100_l8,i100).
isPartOf(i100_l9,i100).
isPartOf(i100_l10,i100).
isPartOf(i100_l11,i100).
isPartOf(i100_l1,i100).
isPartOf(i100_l2,i100).
isPartOf(i100_l3,i100).
isPartOf(i100_l12,i100).
isPartOf(i100_l4,i100).
isPartOf(i100_l5,i100).
isPartOf(i100_l6,i100).
isPartOf(i100_l7,i100).
isPartOf(i100_l13,i100).
isPartOf(i100_l14,i100).
isPartOf(i100_l15,i100).
isPartOf(i100_l16,i100).

% Signal Group that is responsible for Lane
hasSignalGroup(i100_l1,i100_sg4).
hasSignalGroup(i100_l2,i100_sg4).
hasSignalGroup(i100_l5,i100_sg3).
hasSignalGroup(i100_l6,i100_sg3).
hasSignalGroup(i100_l9,i100_sg2).
hasSignalGroup(i100_l10,i100_sg2).
hasSignalGroup(i100_l13,i100_sg1).
hasSignalGroup(i100_l14,i100_sg1).

% Lane connected to Road Segment
connected(i100_l7,rs100_200).
connected(i100_l8,rs100_200).
connected(rs100_200,i100_l9).
connected(rs100_200,i100_l10).

connected(i100_l3,rs100_300).
connected(i100_l4,rs100_300).
connected(rs100_300,i100_l5).
connected(rs100_300,i100_l6).


%%//////////////////////////////
%% Intersection 200 (200)
%%//////////////////////////////

% Lanes and their Geometries
mapLaneIn(i200_l1).
mapLaneIn(i200_l2).
mapLaneOut(i200_l3).
mapLaneOut(i200_l4).
mapLaneIn(i200_l5).
mapLaneIn(i200_l6).
mapLaneOut(i200_l7).
mapLaneOut(i200_l8).
mapLaneIn(i200_l9).
mapLaneIn(i200_l10).
mapLaneOut(i200_l11).
mapLaneOut(i200_l12).
mapLaneIn(i200_l13).
mapLaneIn(i200_l14).
mapLaneOut(i200_l15).
mapLaneOut(i200_l16).

hasGeo(i200_l1,"POLYGON((570 167.8, 595 167.8, 595 171, 570 171, 570 167.8))").
hasGeo(i200_l2,"POLYGON((570 164, 595 164, 595 167.8, 570 167.8, 570 164))").
hasGeo(i200_l5,"POLYGON((604.4 162, 604.4 137, 608 137, 608 162, 604.4 162))").
hasGeo(i200_l6,"POLYGON((608 162, 608 140, 611.3 140, 611.3 162, 608 162))").
hasGeo(i200_l9,"POLYGON((614.3 171.7, 640 171.7, 640 175, 614.3 175.3, 614.3 171.7))").
hasGeo(i200_l10,"POLYGON((614.3 175.3, 640 175, 640 178.5, 614.3 178.5, 614.3 175.3))").
hasGeo(i200_l13,"POLYGON((600.3 182.7, 604 182.7, 604 200, 600.3 200, 600.3 182.7))").
hasGeo(i200_l14,"POLYGON((596.7 182.7, 600 182.7, 600 200, 596.7 200, 596.7 182.7))").

% Lane Connections
connected(i200_l1,i200_l3).
connected(i200_l1,i200_l4).
connected(i200_l1,i200_l7).
connected(i200_l1,i200_l8).
connected(i200_l1,i200_l11).
connected(i200_l1,i200_l12).

connected(i200_l2,i200_l3).
connected(i200_l2,i200_l4).
connected(i200_l2,i200_l7).
connected(i200_l2,i200_l8).
connected(i200_l2,i200_l11).
connected(i200_l2,i200_l12).

connected(i200_l5,i200_l7).
connected(i200_l5,i200_l8).
connected(i200_l5,i200_l11).
connected(i200_l5,i200_l12).
connected(i200_l5,i200_l15).
connected(i200_l5,i200_l16).

connected(i200_l6,i200_l7).
connected(i200_l6,i200_l8).
connected(i200_l6,i200_l11).
connected(i200_l6,i200_l12).
connected(i200_l6,i200_l15).
connected(i200_l6,i200_l16).

connected(i200_l9,i200_l3).
connected(i200_l9,i200_l4).
connected(i200_l9,i200_l11).
connected(i200_l9,i200_l12).
connected(i200_l9,i200_l15).
connected(i200_l9,i200_l16).

connected(i200_l10,i200_l3).
connected(i200_l10,i200_l4).
connected(i200_l10,i200_l11).
connected(i200_l10,i200_l12).
connected(i200_l10,i200_l15).
connected(i200_l10,i200_l16).

connected(i200_l13,i200_l3).
connected(i200_l13,i200_l4).
connected(i200_l13,i200_l7).
connected(i200_l13,i200_l8).
connected(i200_l13,i200_l15).
connected(i200_l13,i200_l16).

connected(i200_l14,i200_l3).
connected(i200_l14,i200_l4).
connected(i200_l14,i200_l7).
connected(i200_l14,i200_l8).
connected(i200_l14,i200_l15).
connected(i200_l14,i200_l16).

% Lane part of Intersection
isPartOf(i200_l1,i200).
isPartOf(i200_l2,i200).
isPartOf(i200_l3,i200).
isPartOf(i200_l4,i200).
isPartOf(i200_l5,i200).
isPartOf(i200_l6,i200).
isPartOf(i200_l7,i200).
isPartOf(i200_l8,i200).
isPartOf(i200_l9,i200).
isPartOf(i200_l10,i200).
isPartOf(i200_l11,i200).
isPartOf(i200_l12,i200).
isPartOf(i200_l13,i200).
isPartOf(i200_l14,i200).
isPartOf(i200_l15,i200).
isPartOf(i200_l16,i200).

% Signal Group that is responsible for Lane
hasSignalGroup(i200_l1,i200_sg2).
hasSignalGroup(i200_l2,i200_sg2).
hasSignalGroup(i200_l5,i200_sg3).
hasSignalGroup(i200_l6,i200_sg3).
hasSignalGroup(i200_l9,i200_sg4).
hasSignalGroup(i200_l10,i200_sg4).
hasSignalGroup(i200_l13,i200_sg1).
hasSignalGroup(i200_l14,i200_sg1).

% Lane connected to Road Segment
connected(i200_l15,rs100_200).
connected(i200_l16,rs100_200).
connected(rs100_200,i200_l1).
connected(rs100_200,i200_l2).

connected(i200_l3,rs200_400).
connected(i200_l4,rs200_400).
connected(rs200_400,i200_l5).
connected(rs200_400,i200_l6).


%%//////////////////////////////
%% Intersection 300 (300)
%%//////////////////////////////

% Lanes and their Geometries
mapLaneIn(i300_l1).
mapLaneIn(i300_l2).
mapLaneOut(i300_l3).
mapLaneOut(i300_l4).
mapLaneIn(i300_l5).
mapLaneIn(i300_l6).
mapLaneOut(i300_l7).
mapLaneOut(i300_l8).
mapLaneIn(i300_l9).
mapLaneIn(i300_l10).
mapLaneOut(i300_l11).
mapLaneOut(i300_l12).
mapLaneIn(i300_l13).
mapLaneIn(i300_l14).
mapLaneOut(i300_l15).
mapLaneOut(i300_l16).

hasGeo(i300_l1,"POLYGON((440 76.2, 465 76.2, 465 79.5, 440 79.5, 440 76.2))").
hasGeo(i300_l2,"POLYGON((440 72.6, 465 72.6, 465 76.2, 440 76.2, 440 72.6))").
hasGeo(i300_l5,"POLYGON((476 68.5, 476 40, 479 40, 479.4 68.5, 476 68.5))").
hasGeo(i300_l6,"POLYGON((479.4 68.5, 479.4 40, 482.5 40, 483 68.5, 479.4 68.5))").
hasGeo(i300_l9,"POLYGON((487.8 80.6, 520 80.6, 520 84, 487.8 84, 487.8 80.6))").
hasGeo(i300_l10,"POLYGON((487.8 84, 520 84, 520 87.4, 487.8 87.4, 487.8 84))").
hasGeo(i300_l13,"POLYGON((473 94.5, 476.2 94.5, 476.5 120, 473 120, 473 94.5))").
hasGeo(i300_l14,"POLYGON((469.3 94.5, 473 94.5, 473 120, 469.5 120, 469.3 94.5))").

% Lane Connections
connected(i300_l1,i300_l3).
connected(i300_l1,i300_l4).
connected(i300_l1,i300_l7).
connected(i300_l1,i300_l8).
connected(i300_l1,i300_l11).
connected(i300_l1,i300_l12).

connected(i300_l2,i300_l3).
connected(i300_l2,i300_l4).
connected(i300_l2,i300_l7).
connected(i300_l2,i300_l8).
connected(i300_l2,i300_l11).
connected(i300_l2,i300_l12).

connected(i300_l5,i300_l7).
connected(i300_l5,i300_l8).
connected(i300_l5,i300_l11).
connected(i300_l5,i300_l12).
connected(i300_l5,i300_l15).
connected(i300_l5,i300_l16).

connected(i300_l6,i300_l7).
connected(i300_l6,i300_l8).
connected(i300_l6,i300_l11).
connected(i300_l6,i300_l12).
connected(i300_l6,i300_l15).
connected(i300_l6,i300_l16).

connected(i300_l9,i300_l3).
connected(i300_l9,i300_l4).
connected(i300_l9,i300_l11).
connected(i300_l9,i300_l12).
connected(i300_l9,i300_l15).
connected(i300_l9,i300_l16).

connected(i300_l10,i300_l3).
connected(i300_l10,i300_l4).
connected(i300_l10,i300_l11).
connected(i300_l10,i300_l12).
connected(i300_l10,i300_l15).
connected(i300_l10,i300_l16).

connected(i300_l13,i300_l3).
connected(i300_l13,i300_l4).
connected(i300_l13,i300_l7).
connected(i300_l13,i300_l8).
connected(i300_l13,i300_l15).
connected(i300_l13,i300_l16).

connected(i300_l14,i300_l3).
connected(i300_l14,i300_l4).
connected(i300_l14,i300_l7).
connected(i300_l14,i300_l8).
connected(i300_l14,i300_l15).
connected(i300_l14,i300_l16).

% Lane part of Intersection
isPartOf(i300_l1,i300).
isPartOf(i300_l2,i300).
isPartOf(i300_l3,i300).
isPartOf(i300_l4,i300).
isPartOf(i300_l5,i300).
isPartOf(i300_l6,i300).
isPartOf(i300_l7,i300).
isPartOf(i300_l8,i300).
isPartOf(i300_l9,i300).
isPartOf(i300_l10,i300).
isPartOf(i300_l11,i300).
isPartOf(i300_l12,i300).
isPartOf(i300_l13,i300).
isPartOf(i300_l14,i300).
isPartOf(i300_l15,i300).
isPartOf(i300_l16,i300).

% Signal Group that is responsible for Lane
hasSignalGroup(i300_l1,i300_sg4).
hasSignalGroup(i300_l2,i300_sg4).
hasSignalGroup(i300_l5,i300_sg2).
hasSignalGroup(i300_l6,i300_sg2).
hasSignalGroup(i300_l9,i300_sg1).
hasSignalGroup(i300_l10,i300_sg1).
hasSignalGroup(i300_l13,i300_sg3).
hasSignalGroup(i300_l14,i300_sg3).

% Lane connected to Road Segment
connected(i300_l11,rs100_300).
connected(i300_l12,rs100_300).
connected(rs100_300,i300_l13).
connected(rs100_300,i300_l14).

connected(i300_l7,rs300_400).
connected(i300_l8,rs300_400).
connected(rs300_400,i300_l9).
connected(rs300_400,i300_l10).


%%//////////////////////////////
%% Intersection 400 (400)
%%//////////////////////////////

% Lanes and their Geometries
mapLaneIn(i400_l1).
mapLaneIn(i400_l2).
mapLaneOut(i400_l3).
mapLaneOut(i400_l4).
mapLaneIn(i400_l5).
mapLaneIn(i400_l6).
mapLaneOut(i400_l7).
mapLaneOut(i400_l8).
mapLaneIn(i400_l9).
mapLaneIn(i400_l10).
mapLaneOut(i400_l11).
mapLaneOut(i400_l12).
mapLaneIn(i400_l13).
mapLaneIn(i400_l14).
mapLaneOut(i400_l15).
mapLaneOut(i400_l16).

hasGeo(i400_l1,"POLYGON((563 76, 592.6 76, 592.6 79.3, 563 79.3, 563 76))").
hasGeo(i400_l2,"POLYGON((563 72.3, 592.6 72.3, 592.6 76, 563 76, 563 72.3))").
hasGeo(i400_l5,"POLYGON((604.6 66, 604.6 21, 608 21, 608 66, 604.6 66))").
hasGeo(i400_l6,"POLYGON((608 66, 608 21, 611.7 21, 611.7 66, 608 66))").
hasGeo(i400_l9,"POLYGON((617.5 80.3, 655 80.3, 655 83.7, 617.5 83.7, 617.5 80.3))").
hasGeo(i400_l10,"POLYGON((617.5 83.7, 655 83.7, 655 87, 617.5 87.2, 617.5 83.7))").
hasGeo(i400_l13,"POLYGON((600.4 92.5, 604 92.5, 604 117, 600.4 117, 600.4 92.5))").
hasGeo(i400_l14,"POLYGON((597 92.5, 600.4 92.5, 600.4 117, 597 117, 597 92.5))").

% Lane Connections
connected(i400_l1,i400_l3).
connected(i400_l1,i400_l4).
connected(i400_l1,i400_l7).
connected(i400_l1,i400_l8).
connected(i400_l1,i400_l11).
connected(i400_l1,i400_l12).

connected(i400_l2,i400_l3).
connected(i400_l2,i400_l4).
connected(i400_l2,i400_l7).
connected(i400_l2,i400_l8).
connected(i400_l2,i400_l11).
connected(i400_l2,i400_l12).

connected(i400_l5,i400_l7).
connected(i400_l5,i400_l8).
connected(i400_l5,i400_l11).
connected(i400_l5,i400_l12).
connected(i400_l5,i400_l15).
connected(i400_l5,i400_l16).

connected(i400_l6,i400_l7).
connected(i400_l6,i400_l8).
connected(i400_l6,i400_l11).
connected(i400_l6,i400_l12).
connected(i400_l6,i400_l15).
connected(i400_l6,i400_l16).

connected(i400_l9,i400_l3).
connected(i400_l9,i400_l4).
connected(i400_l9,i400_l11).
connected(i400_l9,i400_l12).
connected(i400_l9,i400_l15).
connected(i400_l9,i400_l16).

connected(i400_l10,i400_l3).
connected(i400_l10,i400_l4).
connected(i400_l10,i400_l11).
connected(i400_l10,i400_l12).
connected(i400_l10,i400_l15).
connected(i400_l10,i400_l16).

connected(i400_l13,i400_l3).
connected(i400_l13,i400_l4).
connected(i400_l13,i400_l7).
connected(i400_l13,i400_l8).
connected(i400_l13,i400_l15).
connected(i400_l13,i400_l16).

connected(i400_l14,i400_l3).
connected(i400_l14,i400_l4).
connected(i400_l14,i400_l7).
connected(i400_l14,i400_l8).
connected(i400_l14,i400_l15).
connected(i400_l14,i400_l16).

% Lane part of Intersection
isPartOf(i400_l1,i400).
isPartOf(i400_l2,i400).
isPartOf(i400_l3,i400).
isPartOf(i400_l4,i400).
isPartOf(i400_l5,i400).
isPartOf(i400_l6,i400).
isPartOf(i400_l7,i400).
isPartOf(i400_l8,i400).
isPartOf(i400_l9,i400).
isPartOf(i400_l10,i400).
isPartOf(i400_l11,i400).
isPartOf(i400_l12,i400).
isPartOf(i400_l13,i400).
isPartOf(i400_l14,i400).
isPartOf(i400_l15,i400).
isPartOf(i400_l16,i400).

% Signal Group that is responsible for Lane
hasSignalGroup(i400_l1,i400_sg1).
hasSignalGroup(i400_l2,i400_sg1).
hasSignalGroup(i400_l5,i400_sg3).
hasSignalGroup(i400_l6,i400_sg3).
hasSignalGroup(i400_l9,i400_sg4).
hasSignalGroup(i400_l10,i400_sg4).
hasSignalGroup(i400_l13,i400_sg2).
hasSignalGroup(i400_l14,i400_sg2).

% Lane connected to Road Segment
connected(i400_l15,rs300_400).
connected(i400_l16,rs300_400).
connected(rs300_400,i400_l1).
connected(rs300_400,i400_l2).

connected(i400_l11,rs200_400).
connected(i400_l12,rs200_400).
connected(rs200_400,i400_l13).
connected(rs200_400,i400_l14).


% Connections (not needed since road segments fullfill this)

% connected(i100,i200).
% connected(i100,i300).
% connected(i200,i400).
% connected(i300,i400).


% Vehicles and Traffic Lights

car(car_1).
car(car_2).
car(car_3).
car(car_4).
car(car_5).
car(car_300).
vehicle(mot_1).
vehicle(mot_2).
vehicle(mot_3).
bus(bus_1).
vehicleMaker(car_1,"BMW").
vehicleMaker(car_2,"VW").

% Streaming data (3 position is time stamp, e.g., 1001)

speed(car_1,20,1001).
speed(car_1,20,1002).
pos(car_1,"POINT(0,0)",1001).
pos(car_1,"POINT(3,3)",1002).
heading(car_1,32,1001).
heading(car_1,33,1002).

signalState(i100_sg1,0,1001).
signalState(i100_sg1,0,1002).
signalState(i100_sg2,1,1001).
signalState(i100_sg2,1,1002).



