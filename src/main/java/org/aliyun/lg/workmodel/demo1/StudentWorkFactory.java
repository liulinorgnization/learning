package org.aliyun.lg.workmodel.demo1;
public class StudentWorkFactory implements IWorkFactory {

    public Work getWork() {
        return new StudentWork();
    }

}
