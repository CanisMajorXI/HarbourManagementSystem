

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ContainerSample {
    private int row = 3, column = 10, layer = 3;
    Random random = new Random();
    private List<Container> contaners = new ArrayList<>();

    private void run() {
        for (int i = 1; i <= row; i++) {
            generate(1, column, i, 1);
        }
    }

    private void print() {
        contaners.sort(new Comparator<Container>() {
            @Override
            public int compare(Container o1, Container o2) {
                if (o1.row != o2.row) {
                    if (o1.row < o2.row) return -1;
                    else return 1;
                }
                if (o1.layer != o2.layer) {
                    if (o1.layer < o2.layer) return -1;
                    else return 1;
                }
                if (o1.column <= o2.column) return -1;
                else return 1;
            }
        });
        for (Container container : contaners) {
            System.out.println("列："+container.column + "  层：" +container.layer + "  行："+ container.row+ "  类型："+ container.size);
        }
    }


    private void generate(int begin, int end, int row, int layer) {
        if (layer > this.layer) return;
        int curpos = begin;
        int flag = 1;
        while (curpos <= end) {
            int rd = random.nextInt(10);
            if (rd <= 3) {
                if (flag == 1) {
                    flag = 0;
                    begin = curpos;
                }
                int type = random.nextInt(3);
                contaners.add(new Container(row, curpos, layer, type, 0));
                curpos++;
            } else {
                if (rd > 6 || curpos == end) {
                    if (flag == 0) {
                        generate(begin, curpos - 1, row, layer + 1);
                    }
                    flag = 1;
                    curpos++;
                } else {
                    if (flag == 1) {
                        flag = 0;
                        begin = curpos;
                    }
                    int type = random.nextInt(3);
                    contaners.add(new Container(row, curpos, layer, type, 1));
                    curpos += 2;
                }
            }
        }
        generate(begin, curpos - 1, row, layer + 1);
    }

    private class Container {
        public Container(int row, int column, int layer, int type, int size) {
            this.row = row;
            this.column = column;
            this.layer = layer;
            this.type = type;
            this.size = size;
        }

        private int row, column, layer, type, size;

    }

    public static void main(String[] args) {
        ContainerSample sample = new ContainerSample();
        sample.run();
        sample.print();

    }
}
