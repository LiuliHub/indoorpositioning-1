set line style 1 lt 1 lw 1
set line style 2 lt 5 lw 1
set line style 3 lt 2 lw 1
set line style 4 lt 7 lw 1
set line style 5 lt 3 lw 1
set line style 6 lt 4 lw 1
set line style 7 lt 5 lw 1
set line style 8 lt 6 lw 1
set line style 9 lt 9 lw 1
set grid

set rmargin 1.5
set tmargin 1.5
set lmargin 10

set xrange [*:*]
set xtics rotate by -90
set xlabel "Zeit"

set key r c
set yrange [*:*]
set ylabel "Beschleunigung" -1
set out "acceleration.emf"
plot \
"acceleration.data" 	u ($1):($3 + $4) t "xyz" w lp ls 1;