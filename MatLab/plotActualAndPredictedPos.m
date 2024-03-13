A = readmatrix('result_actualPos_VRU1.txt');
P2 = readmatrix('result_predPos_2DPs_VRU1.txt');
P3 = readmatrix('result_predPos_3DPs_VRU1.txt');
P4 = readmatrix('result_predPos_4DPs_VRU1.txt');
%R = readmatrix('result_RMSE_allVRUs_dataPoint.txt');
xA = A(:,3);
yA = A(:,4);
xP2 = P2(:,3);
yP2 = P2(:,4);
xP3 = P3(:,3);
yP3 = P3(:,4);
xP4 = P4(:,3);
yP4 = P4(:,4);
%RMSE = R(1,3);
%err = repelem(RMSE,25);

%errorbar(xP,yP,err,"both","o")
plot(xA,yA,'-o','LineWidth',1);
hold on;
plot(xP2,yP2,'-x');
plot(xP3,yP3,'-x');
plot(xP4,yP4,'-x','color','green');
hold off;
title('Actual, -and predicted position on VRUs. Scenario: Straight Line');
ylabel('y-position');
xlabel('x-position');
legend('Location','northeast');
legend('Actual position','Pred. pos 2DPs','Pred. pos 3DPs','Pred. pos 4DPs');