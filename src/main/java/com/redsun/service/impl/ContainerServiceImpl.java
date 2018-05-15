package com.redsun.service.impl;

import com.redsun.dao.CargoMapper;
import com.redsun.dao.ContainerMapper;
import com.redsun.pojo.Cargo;
import com.redsun.pojo.Container;
import com.redsun.service.CargoService;
import com.redsun.service.ContainerService;
import com.redsun.util.ContainerTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ContainerServiceImpl implements ContainerService {
    @Autowired
    private ContainerMapper containerMapper = null;
    @Autowired
    private CargoMapper cargoMapper = null;
    @Autowired
    private CargoService cargoService = null;


    /**
     * 未实现
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Container> getContainers() {
        List<Container> containers = null;
        try {
            containers = containerMapper.getTotalContainers();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            return containers;
        }
    }

    /**
     * 获取箱子信息
     *
     * @param container
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Container> getContainers(Container container) {
        List<Container> resultContainers = null;
        resultContainers = containerMapper.getContainers(container);
        return resultContainers;
    }

    /**
     * 添加一个空箱子
     *
     * @param container
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean addAnEmptyContainer(Container container) {
        containerMapper.insertContainer(container);
        return true;
    }


    //检查插入的位置是否可插入，例如放在高层的时候底层必须有集装箱
    private boolean isPosEnabled(Container con) {
        List<Container> containers = containerMapper.getContainers(new Container.Builder().setRow(con.getRow()).build());
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addAContainerWithCargo(Container container, List<Cargo> cargos) {
        containerMapper.insertContainer(container);
        for (Cargo cargo : cargos) {
            cargo.setContainerId(container.getId());
            cargoMapper.insertCargo(cargo);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void changeContainerPosition(Integer id, Byte row, Byte column, Byte layer) {

        //todo位置检查
        containerMapper.updateContainerPosition(id, row, column, layer);
    }

    /**
     * 获取所有可插入的位置
     * @param container
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Container> showFeasibleArea(Container container) {
        /**
         * Containers    已经存在的箱子
         * result        可插入箱子的位置
         * containerTools 存放箱子信息
         * ContainerSize 当前要插入箱子的长度
         */
        Container ct = new Container();
        List<Container> Containers = containerMapper.getContainers(ct);
        List<Container> result = new ArrayList<>();
        List<ContainerTool> containerTools = new ArrayList<>();
        Byte ContaienrSize = container.getSize();
        int count = 0;
        //cont 用来标记箱子


        if(cargoService.isEmpty(container)){
            //空箱
            int cont[][][] = new int[5][10][6];
            for(int i=0;i<5;i++)
                for(int j=0;j<10;j++)
                    for(int k=0;k<6;k++)
                        cont[i][j][k] = 0;
            /**
             * step 1
             * 获取所有空箱区的箱子的行列层数以及箱子尺寸加入到containerTools链表中
             */
            for (Container container3: Containers
                    ) {
                Byte row = container3.getRow();
                Byte column = container3.getColumn();
                Byte layer = container3.getLayer();
                Byte size = container3.getSize();
                if(row >= (byte)1 && row <=(byte)5){
                    ContainerTool containerTool = new ContainerTool();
                    containerTool.setColumn(column);
                    containerTool.setLayer(layer);
                    containerTool.setRow(row);
                    containerTool.setSize(size);
                    containerTools.add(containerTool);
                }
            }

            /**
             * step 2
             * 标记所有箱子的位置 设置为1
             * 对大箱进行拆分 -- 相当于拆分成两个箱子
             */
            for (ContainerTool containertool:containerTools
                    ) {
                Byte row = containertool.getRow();
                Byte column = containertool.getColumn();
                Byte layer = containertool.getLayer();
                Byte size = containertool.getSize();
                if(size == 0)
                    cont[row-1][column-1][layer-1] = 1;
                else if(size == 1){
                    cont[row-1][column-1][layer-1] = 1;
                    cont[row-1][column][layer-1] = 1;
                }
            }
            /**
             * step 3
             * 第一层可放箱子的区域设置
             */
            for(int i=0;i<5;i++)
                for(int j=0;j<10;j++){
                    if(cont[i][j][0]  == 0){
                        cont[i][j][0] = 2;
                    }
                }
            /**
             * step 4
             * 高层可放箱子的区域设置
             */
            for(int i=0;i<5;i++)
                for(int j=0;j<10;j++)
                    for(int k=0;k<6;k++){
                        if(cont[i][j][k] == 1 && k<= 4){
                            if(cont[i][j][k+1] == 0){
                                cont[i][j][k+1] = 2;
                            }
                        }
                    }

            /**
             * step 5
             * 根据尺寸进行合并 （大箱）
             */
            if(ContaienrSize == 1){
                for(int i=0;i<5;i++)
                    for(int j=0;j<10;j++)
                        for(int k=0;k<6;k++){
                            if(cont[i][j][k] == 2 && j<= 8){
                                if(cont[i][j+1][k] == 2){
                                    //cont[i][j][k] = 2;
                                    //  cont[i][j+1][k] = 1; //相当于进行了插入
                                }else{
                                    cont[i][j][k] = 0;
                                }
                            }else if(cont[i][j][k] == 2 && j==9){
                                cont[i][j][k] = 0;//处理边缘数据
                            }
                        }

            }
            /**
             * step 6
             * 遍历可插入的箱子位置
             */
            for(int i=0;i<5;i++)
                for(int j=0;j<10;j++)
                    for(int k=0;k<6;k++){
                        if(cont[i][j][k]  == 2 ) {
                            Container container1 = new Container();
                            container1.setRow((byte) (i + 1));
                            container1.setColumn((byte) (j + 1));
                            container1.setLayer((byte) (k + 1));
                            result.add(container1);
                        }
                    }

            return result;

        }else { //不是空箱，根据类型获取
            if(container.getType() == Container.TYPE_ORDINARY){
                //普通
                int cont[][][] = new int[Container.TOTAL_ROWS_ORDINARY][Container.TOTAL_COLUMNS_ORDINARY][Container.TOTAL_LAYERS_ORDINARY];
                for(int i=0;i<Container.TOTAL_ROWS_ORDINARY;i++)
                    for(int j=0;j<Container.TOTAL_COLUMNS_ORDINARY;j++)
                        for(int k=0;k<Container.TOTAL_LAYERS_ORDINARY;k++)
                            cont[i][j][k] = 0;
                return null;
            }else if(container.getType() == Container.TYPE_FREEZE){
                //冷冻
                return null;
            }else{
                //危险
                return null;
            }
        }

    }
}

