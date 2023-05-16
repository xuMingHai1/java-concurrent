package xyz.xuminghai.blocking_queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.StringJoiner;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 2023/5/10 22:09 星期三<br/>
 * <h1>PriorityBlockingQueue示例02</h1>
 *
 * @author xuMingHai
 */
public class PriorityBlockingQueueDemo02 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PriorityBlockingQueueDemo02.class);


    /**
     * 自定义优先级策略
     */
    private static final PriorityBlockingQueue<Student> PRIORITY_BLOCKING_QUEUE = new PriorityBlockingQueue<>(10,
            Comparator.comparingInt(o -> o.score));

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            PRIORITY_BLOCKING_QUEUE.put(Student.studentFactory());
        }
        for (int i = 0; i < 10; i++) {
            LOGGER.info(String.valueOf(PRIORITY_BLOCKING_QUEUE.poll()));
        }
    }

    private static class Student {
        private static final int CODE_POINT = 'Z' - 'A' + 1;
        private final String name;
        private final int score;

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public static Student studentFactory() {
            int nameLength = (int) (Math.random() * 5) + 1;
            StringBuilder name = new StringBuilder(nameLength);
            for (int i = 0; i < nameLength; i++) {
                char c = (char) ((int) (Math.random() * CODE_POINT) + 'A');
                name.append(c);
            }
            return new Student(name.toString(), (int) (Math.random() * 100) + 1);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("score=" + score)
                    .toString();
        }
    }


}
