package kr.co.devhanjong.hjtb_coin_stream_daemon.memoryDb;

import kr.co.devhanjong.hjtb_coin_stream_daemon.model.HogaMemoryDbKey;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class HogaMemoryDb {
    private final ConcurrentHashMap<HogaMemoryDbKey, Integer> hogaDb = new ConcurrentHashMap<>();

    public void setSortNo(String vaspSimpleName, String mySymbol, Integer sortNo) {
        HogaMemoryDbKey hogaMemoryDbKey = HogaMemoryDbKey.builder()
                .vaspSimpleName(vaspSimpleName)
                .mySymbol(mySymbol)
                .build();
        hogaDb.put(hogaMemoryDbKey, sortNo);
    }

    public Integer getSortNo(String vaspSimpleName, String mySymbol) {
        HogaMemoryDbKey hogaMemoryDbKey = HogaMemoryDbKey.builder()
                .vaspSimpleName(vaspSimpleName)
                .mySymbol(mySymbol)
                .build();
        return hogaDb.get(hogaMemoryDbKey);
    }

//    public void removeHoga(String vaspSimpleName, String mySymbol) {
//        HogaMemoryDbKey hogaMemoryDbKey = HogaMemoryDbKey.builder()
//                .vaspSimpleName(vaspSimpleName)
//                .mySymbol(mySymbol)
//                .build();
//        hogaDb.remove(hogaMemoryDbKey);
//    }
}
