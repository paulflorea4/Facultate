//package org.example.ducksocialnetworkui.factory;
//
//import org.example.ducksocialnetworkui.domain.Duck;
//import org.example.ducksocialnetworkui.domain.Inotator;
//import org.example.ducksocialnetworkui.domain.SwimmingDuck;
//import org.example.ducksocialnetworkui.event.Event;
//import org.example.ducksocialnetworkui.event.RaceEvent;
//
//import java.util.List;
//
//public class EventFactory implements Factory<Event> {
//
//    private static EventFactory instance;
//
//    private EventFactory() {}
//    @SuppressWarnings("unchecked")
//    public static synchronized <T extends Duck & Inotator> EventFactory getInstance() {
//        if (instance == null) {
//            instance = new EventFactory();
//        }
//        return instance;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public Event create(Object... params) {
//
//        Long id = (Long) params[0];
//        String name = (String) params[1];
//        List<Integer> distances = (List<Integer>) params[2];
//        List<SwimmingDuck> selected = (List<SwimmingDuck>) params[3];
//
//        return new RaceEvent(id, name, distances,selected);
//    }
//}
