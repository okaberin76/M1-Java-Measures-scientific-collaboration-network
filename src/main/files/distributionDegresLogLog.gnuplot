set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'distributionDegresLogLog.png'
set key top right
set yrange [1e-6:1]
set logscale xy 10

plot  "fichierDistributionDegree.dat" using 1:2 title 'Distribution des degrés en log log' with lines