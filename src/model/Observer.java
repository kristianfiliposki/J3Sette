package model;

import model.Observable;

public interface Observer {
    void update(Observable observable, Object arg);
}
