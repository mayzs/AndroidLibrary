package com.cctbn.baselibrary.common.picture.observable;


import com.cctbn.baselibrary.common.picture.entity.LocalMedia;
import com.cctbn.baselibrary.common.picture.entity.LocalMediaFolder;

import java.util.List;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.observable
 * email：893855882@qq.com
 * data：17/1/16
 */
public interface ObserverListener {
    void observerUpFoldersData(List<LocalMediaFolder> folders);

    void observerUpSelectsData(List<LocalMedia> selectMedias);
}
