set terminal png
set xlabel 'Days'
set xrange[0:91]
set ylabel 'Infected'
set yrange[0:0.99]
set output 'propagationDefault_All_Percent.PNG'
set title 'Default - All'
set key top left

plot "fichierPropagationDefault_Scenario1_Percent.dat" using 1:2 title 'S1' with line, \
     "fichierPropagationDefault_Scenario2_Percent.dat" using 1:2 title 'S2' with line, \
     "fichierPropagationDefault_Scenario3_Percent.dat" using 1:2 title 'S3' with line
