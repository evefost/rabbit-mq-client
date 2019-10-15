package test.com.eve;

public class SubClass implements SuperClass<String> {
    @Override
    public String method(String param) {
        return param;
    }
}