package express_java.practice_7.optional_tasks.task_3;

import java.util.ArrayList;
import java.util.List;

public class NumberBox <T extends Number> {
    List<T> list;

    public NumberBox(List<T> list) {
        this.list = list;
    }

    public NumberBox() {
        list = new ArrayList<>();
    }

    public void addElement(T element) {
        list.add(element);
    }

    public double getSum() {
        double sum = 0;
        for (T element : list) {
            sum += element.doubleValue();
        }
        return sum;
    }
}
