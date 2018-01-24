# Fast Log Block
## Event multithread handling
![](https://image.ibb.co/hyaPRw/Fast_Log_Block.png)

## Log file format

|         Name         |  posX  |  posY  |  posZ  |                                 typeaction                                 | playerid | blockid | timestamp |
|:--------------------:|:------:|:------:|:------:|:--------------------------------------------------------------------------:|:--------:|:-------:|:---------:|
| Field Length (bytes) | 4 byte | 4 byte | 4 byte | 1 byte ('0' for Remove, '1' for Insert, '2' for update, '100' for unknown) |  4 byte  |  8 byte |   8 byte  |

Total: 33 bytes per line

Filename: /{save}/{world/dimension}/*.bytelog

|         Name         |   id   |      blockname     |
|:--------------------:|:------:|:------------------:|
| Field Length (bytes) | 8 byte | 1 byte per symbols |

Total: ~ 21 bytes per block

Filename: blockmap.bytelog

| Name | id | nickname |
|:--------------------:|:------:|:------------------:|
| Field Length (bytes) | 4 byte | 1 byte per symbols |

Total: ~ 10 bytes per Player

Filename: nickmap.bytelog

