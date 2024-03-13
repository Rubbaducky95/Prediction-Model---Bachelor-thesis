T = readmatrix('result_minMax_RMSE_dataPoints.txt');
x = [1;T(:,1);8];   
y = linspace(0,0,8);
errMin = [0;T(:,2);0];
errMax = [0;T(:,3);0];
errorbar(x,y,errMax, "o")
hold on;
errorbar(x,y,errMin, "o");
hold off;

title('RMSE for different amount of data points, Scenario: Straight line')
xlabel('No. data points');
ylabel('Approximate distance from actual position');
legend('Location','northwest');
legend('max RMSE','min RMSE');