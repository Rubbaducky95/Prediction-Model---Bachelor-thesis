D = readmatrix('differenceInPos.txt');
x = D(:,1);
edges = [0 0.05:0.1:3.9 3.95];
h = histogram(x);
h.Normalization = 'countdensity';
title('All distances from predicted, -to actual position');
xlabel('Distance from actual position');
ylabel('No. occurances');