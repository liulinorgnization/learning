package org.aliyun.lg.workmodel.demo1;
public class TeacherWorkFactory implements IWorkFactory {

    public Work getWork() {
        return new TeacherWork();
    }

}
