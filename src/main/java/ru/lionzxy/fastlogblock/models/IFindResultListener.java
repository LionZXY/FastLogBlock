package ru.lionzxy.fastlogblock.models;

import java.util.List;

public interface IFindResultListener {
    void onResultAsync(List<BlockChangeEventModel> blockChangeEventModels);
}
