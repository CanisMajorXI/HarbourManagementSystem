package com.redsun.service.impl;

import com.redsun.dao.CargoMapper;
import com.redsun.dao.ContainerMapper;
import com.redsun.dao.ShipperCargoMapper;
import com.redsun.dao.ShipperContainerMapper;
import com.redsun.pojo.Cargo;
import com.redsun.pojo.Container;
import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;
import com.redsun.service.CargoService;
import com.redsun.service.ContainerService;
import com.redsun.service.ShipperCargoService;
import com.redsun.util.ContainerTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerMapper containerMapper = null;

    @Autowired
    private CargoMapper cargoMapper = null;

    @Autowired
    private ShipperContainerMapper shipperContainerMapper = null;

    @Autowired
    private ShipperCargoMapper shipperCargoMapper = null;

    @Autowired
    private CargoService cargoService = null;

    public static final int TYPE_TASK = 0;
    public static final int TYPE_REPO = 1;

    /**
     * 未实现
     *
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
    public void importCargoIntoEmptyContainer(Integer shipperCargoId,
                                              Integer containerId,
                                              Byte row, Byte column, Byte layer) {
        ShipperCargo tempShipperCargo = new ShipperCargo();
        tempShipperCargo.setCargoId(shipperCargoId);
        ShipperCargo shipperCargo = shipperCargoMapper.getShipperCargos(tempShipperCargo).get(0);
        Container container = containerMapper.getContainers(new Container.Builder().setId(containerId).build()).get(0);
        Cargo cargo = new Cargo();
        cargo.setContainerId(container.getId());
        cargo.setTypeId(shipperCargo.getCargoTypeId());
        cargo.setGross(shipperCargo.getGross());
        cargo.setCargoId(shipperCargoId);
        cargoMapper.insertCargo(cargo);
        shipperCargoMapper.deleteShipperCargo(shipperCargo.getCargoId());
        changeContainerPosition(containerId, row, column, layer);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    @Override
    public void changeContainerPosition(Integer id, Byte row, Byte column, Byte layer) {
//        Container uninsertedContainer = containerMapper.getContainers(new Container.Builder().setId(id).build()).get(0);
//        //todo位置检查
//        List<Container> containers = showFeasibleArea(uninsertedContainer, ContainerServiceImpl.TYPE_REPO);
//        if (!ContainerTool.isPostionInArea(containers, row, column, layer)) {
//            System.out.println("不可插入!");
//            throw new RuntimeException();
//        }
        containerMapper.updateContainerPosition(id, row, column, layer);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void importAnEmptyContainerFromTask(Integer shipperContainerId, Byte row, Byte column, Byte layer) {
        //判断该位置可以放置箱子

        //todo
        ShipperContainer shipperContainer = new ShipperContainer();
        shipperContainer.setContainerId(shipperContainerId);
        List<ShipperContainer> shipperContainers = shipperContainerMapper.getShipperContainers(shipperContainer);
        if (shipperContainers.size() == 0) throw new RuntimeException();
        shipperContainer = shipperContainers.get(0);
        Container unisertContainer = new Container.Builder().setId(shipperContainerId).setSize(shipperContainer.getSize()).setType(shipperContainer.getType()).build();
        List<Container> containers = showFeasibleArea(unisertContainer, ContainerServiceImpl.TYPE_TASK);
        if (!ContainerTool.isPostionInArea(containers, row, column, layer)) {
            System.out.println("不可插入!");
            throw new RuntimeException();
        }
//        for (Container container : containers) {
//            System.out.print("row" + container.getRow() + " ");
//            System.out.print("column" + container.getColumn() + " ");
//            System.out.println("layer" + container.getLayer());
//        }
        Container container = new Container();
        container.setId(shipperContainer.getContainerId());
        container.setLayer(layer);
        container.setRow(row);
        container.setColumn(column);
        container.setSize(shipperContainer.getSize());
        container.setType(shipperContainer.getType());
        containerMapper.insertContainer(container);
        shipperContainerMapper.deleteShipperContainerById(shipperContainer.getContainerId());


    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void importAContainerWithCargosFromTask(Integer shipperContainerId, Byte row, Byte column, Byte layer) {
        //判断该位置可以放置箱子
        //todo
        ShipperContainer shipperContainer = new ShipperContainer();
        shipperContainer.setContainerId(shipperContainerId);
        List<ShipperContainer> shipperContainers = shipperContainerMapper.getShipperContainers(shipperContainer);
        if (shipperContainers.size() == 0) throw new RuntimeException();
        ShipperCargo shipperCargoForId = new ShipperCargo();
        shipperCargoForId.setContainerId(shipperContainerId);
        List<ShipperCargo> shipperCargos = shipperCargoMapper.getShipperCargos(shipperCargoForId);
        shipperContainer = shipperContainers.get(0);
        Container unisertContainer = new Container.Builder().setId(shipperContainerId).setSize(shipperContainer.getSize()).setType(shipperContainer.getType()).build();
        List<Container> containers = showFeasibleArea(unisertContainer, ContainerServiceImpl.TYPE_TASK);
        if (!ContainerTool.isPostionInArea(containers, row, column, layer)) {
            System.out.println("不可插入!");
            throw new RuntimeException();
        }
        Container container = new Container();
        container.setId(shipperContainer.getContainerId());
        container.setLayer(layer);
        container.setRow(row);
        container.setColumn(column);
        container.setSize(shipperContainer.getSize());
        container.setType(shipperContainer.getType());
        containerMapper.insertContainer(container);
        for (ShipperCargo shipperCargo : shipperCargos) {
            Cargo cargo = new Cargo();
            cargo.setCargoId(shipperCargo.getCargoId());
            cargo.setGross(shipperCargo.getGross());
            cargo.setTypeId(shipperCargo.getCargoTypeId());
            cargo.setContainerId(shipperContainer.getContainerId());
            cargoMapper.insertCargo(cargo);
            shipperCargoMapper.deleteShipperCargo(shipperCargo.getCargoId());
        }
        shipperContainerMapper.deleteShipperContainerById(shipperContainer.getContainerId());

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void importCargoIntoContainerFromTask(ShipperCargo shipperCargo, Container selectedContainer, Container containerForPos) {

    }

    /**
     * 获取所有可插入的位置
     *
     * @param container
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Container> showFeasibleArea(Container container, int flag) {
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
        boolean isEmpty = false;
        //如果是从任务去加到仓库，判断是否为空需要查询任务区的货物
        if (flag == TYPE_TASK)
            isEmpty = cargoService.isEmptyInTask(container);
        else isEmpty = cargoService.isEmpty(container);
        int idd = 10;
        if (isEmpty) {
            //空箱
            int cont[][][] = new int[Container.TOTAL_ROWS_EMPTY][Container.TOTAL_COLUMNS_EMPTY][Container.TOTAL_LAYERS_EMPTY];
            for (int i = 0; i < Container.TOTAL_ROWS_EMPTY; i++)
                for (int j = 0; j < Container.TOTAL_COLUMNS_EMPTY; j++)
                    for (int k = 0; k < Container.TOTAL_LAYERS_EMPTY; k++)
                        cont[i][j][k] = 0;
            /**
             * step 1
             * 获取所有空箱区的箱子的行列层数以及箱子尺寸加入到containerTools链表中
             */
            for (Container container3 : Containers
                    ) {
                Byte row = container3.getRow();
                Byte column = container3.getColumn();
                Byte layer = container3.getLayer();
                Byte size = container3.getSize();
                if (row >= Container.LOWER_LIMIT_EMPTY && row <= Container.UPPER_LIMIT_EMPTY) {
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
            for (ContainerTool containertool : containerTools
                    ) {
                Byte row = containertool.getRow();
                Byte column = containertool.getColumn();
                Byte layer = containertool.getLayer();
                Byte size = containertool.getSize();
                if (size == 0)
                    cont[row - 1][column - 1][layer - 1] = 1;
                else if (size == 1) {
                    cont[row - 1][column - 1][layer - 1] = 1;
                    cont[row - 1][column][layer - 1] = 1;
                }
            }
            /**
             * step 3
             * 第一层可放箱子的区域设置
             */
            for (int i = 0; i < Container.TOTAL_ROWS_EMPTY; i++)
                for (int j = 0; j < Container.TOTAL_COLUMNS_EMPTY; j++) {
                    if (cont[i][j][0] == 0) {
                        cont[i][j][0] = 2;
                    }
                }
            /**
             * step 4
             * 高层可放箱子的区域设置
             */
            for (int i = 0; i < Container.TOTAL_ROWS_EMPTY; i++)
                for (int j = 0; j < Container.TOTAL_COLUMNS_EMPTY; j++)
                    for (int k = 0; k < Container.TOTAL_LAYERS_EMPTY; k++) {
                        if (cont[i][j][k] == 1 && k <= 4) {
                            if (cont[i][j][k + 1] == 0) {
                                cont[i][j][k + 1] = 2;
                            }
                        }
                    }

            /**
             * step 5
             * 根据尺寸进行合并 （大箱）
             */
            if (ContaienrSize == 1) {
                for (int i = 0; i < Container.TOTAL_ROWS_EMPTY; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_EMPTY; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_EMPTY; k++) {
                            if (cont[i][j][k] == 2 && j <= (Container.TOTAL_COLUMNS_EMPTY - 2)) {
                                if (cont[i][j + 1][k] == 2) {
                                    //cont[i][j][k] = 2;
                                    //  cont[i][j+1][k] = 1; //相当于进行了插入
                                } else {
                                    cont[i][j][k] = 0;
                                }
                            } else if (cont[i][j][k] == 2 && j == 9) {
                                cont[i][j][k] = 0;//处理边缘数据
                            }
                        }

            }
            /**
             * step 6
             * 遍历可插入的箱子位置
             */
            for (int i = 0; i < Container.TOTAL_ROWS_EMPTY; i++)
                for (int j = 0; j < Container.TOTAL_COLUMNS_EMPTY; j++)
                    for (int k = 0; k < Container.TOTAL_LAYERS_EMPTY; k++) {
                        if (cont[i][j][k] == 2) {
                            Container container1 = new Container();
                            container1.setRow((byte) (i + 1));
                            container1.setColumn((byte) (j + 1));
                            container1.setLayer((byte) (k + 1));
                            result.add(container1);
                        }
                    }

            return result;

        } else { //不是空箱，根据类型获取
            if (container.getType() == Container.TYPE_ORDINARY) {
                //普通
                int cont[][][] = new int[Container.TOTAL_ROWS_ORDINARY][Container.TOTAL_COLUMNS_ORDINARY][Container.TOTAL_LAYERS_ORDINARY];
                for (int i = 0; i < Container.TOTAL_ROWS_ORDINARY; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_ORDINARY; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_ORDINARY; k++)
                            cont[i][j][k] = 0;
                /**
                 * step 1
                 * 获取所有空箱区的箱子的行列层数以及箱子尺寸加入到containerTools链表中
                 */
                for (Container container3 : Containers
                        ) {
                    Byte row = container3.getRow();
                    Byte column = container3.getColumn();
                    Byte layer = container3.getLayer();
                    Byte size = container3.getSize();
                    if (row >= Container.LOWER_LIMIT_ORDINARY && row <= Container.UPPER_LIMIT_ORDINARY) {
                        ContainerTool containerTool = new ContainerTool();
                        containerTool.setColumn(column);
                        containerTool.setLayer(layer);
                        containerTool.setRow((byte) (row - Container.UPPER_LIMIT_EMPTY));
                        containerTool.setSize(size);
                        containerTools.add(containerTool);
                    }
                }

                /**
                 * step 2
                 * 标记所有箱子的位置 设置为1
                 * 对大箱进行拆分 -- 相当于拆分成两个箱子
                 */
                for (ContainerTool containertool : containerTools
                        ) {
                    Byte row = containertool.getRow();
                    Byte column = containertool.getColumn();
                    Byte layer = containertool.getLayer();
                    Byte size = containertool.getSize();
                    if (size == 0)
                        cont[row - 1][column - 1][layer - 1] = 1;
                    else if (size == 1) {
                        cont[row - 1][column - 1][layer - 1] = 1;
                        cont[row - 1][column][layer - 1] = 1;
                    }
                }
                /**
                 * step 3
                 * 第一层可放箱子的区域设置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_ORDINARY; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_ORDINARY; j++) {
                        if (cont[i][j][0] == 0) {
                            cont[i][j][0] = 2;
                        }
                    }
                /**
                 * step 4
                 * 高层可放箱子的区域设置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_ORDINARY; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_ORDINARY; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_ORDINARY; k++) {
                            if (cont[i][j][k] == 1 && k <= (Container.TOTAL_LAYERS_ORDINARY - 2)) {
                                if (cont[i][j][k + 1] == 0) {
                                    cont[i][j][k + 1] = 2;
                                }
                            }
                        }

                /**
                 * step 5
                 * 根据尺寸进行合并 （大箱）
                 */
                if (ContaienrSize == 1) {
                    for (int i = 0; i < Container.TOTAL_ROWS_ORDINARY; i++)
                        for (int j = 0; j < Container.TOTAL_COLUMNS_ORDINARY; j++)
                            for (int k = 0; k < Container.TOTAL_LAYERS_ORDINARY; k++) {
                                if (cont[i][j][k] == 2 && j <= (Container.TOTAL_COLUMNS_ORDINARY - 2)) {
                                    if (cont[i][j + 1][k] == 2) {
                                        //cont[i][j][k] = 2;
                                        //  cont[i][j+1][k] = 1; //相当于进行了插入
                                    } else {
                                        cont[i][j][k] = 0;
                                    }
                                } else if (cont[i][j][k] == 2 && j == (Container.TOTAL_COLUMNS_ORDINARY - 1)) {
                                    cont[i][j][k] = 0;//处理边缘数据
                                }
                            }

                }
                /**
                 * step 6
                 * 遍历可插入的箱子位置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_ORDINARY; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_ORDINARY; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_ORDINARY; k++) {
                            if (cont[i][j][k] == 2) {
                                Container container1 = new Container();
                                container1.setRow((byte) (i + Container.LOWER_LIMIT_ORDINARY)); //行数加6
                                container1.setColumn((byte) (j + 1));
                                container1.setLayer((byte) (k + 1));
                                result.add(container1);
                            }
                        }

                return result;
            } else if (container.getType() == Container.TYPE_FREEZE) {
                //冷冻
                int cont[][][] = new int[Container.TOTAL_ROWS_FREEZE][Container.TOTAL_COLUMNS_FREEZE][Container.TOTAL_LAYERS_FREEZE];
                for (int i = 0; i < Container.TOTAL_ROWS_FREEZE; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_FREEZE; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_FREEZE; k++)
                            cont[i][j][k] = 0;
                /**
                 * step 1
                 * 获取所有箱子的行列层数以及箱子尺寸加入到containerTools链表中
                 */
                for (Container container3 : Containers
                        ) {
                    Byte row = container3.getRow();
                    Byte column = container3.getColumn();
                    Byte layer = container3.getLayer();
                    Byte size = container3.getSize();
                    if (row >= Container.LOWER_LIMIT_FREEZE && row <= Container.UPPER_LIMIT_FREEZE) {
                        ContainerTool containerTool = new ContainerTool();
                        containerTool.setColumn(column);
                        containerTool.setLayer(layer);
                        containerTool.setRow((byte) (row - Container.UPPER_LIMIT_ORDINARY));
                        containerTool.setSize(size);
                        containerTools.add(containerTool);
                    }
                }

                /**
                 * step 2
                 * 标记所有箱子的位置 设置为1
                 * 对大箱进行拆分 -- 相当于拆分成两个箱子
                 */
                for (ContainerTool containertool : containerTools
                        ) {
                    Byte row = containertool.getRow();
                    Byte column = containertool.getColumn();
                    Byte layer = containertool.getLayer();
                    Byte size = containertool.getSize();
                    if (size == 0)
                        cont[row - 1][column - 1][layer - 1] = 1;
                    else if (size == 1) {
                        cont[row - 1][column - 1][layer - 1] = 1;
                        cont[row - 1][column][layer - 1] = 1;
                    }
                }
                /**
                 * step 3
                 * 第一层可放箱子的区域设置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_FREEZE; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_FREEZE; j++) {
                        if (cont[i][j][0] == 0) {
                            cont[i][j][0] = 2;
                        }
                    }
                /**
                 * step 4
                 * 高层可放箱子的区域设置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_FREEZE; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_FREEZE; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_FREEZE; k++) {
                            if (cont[i][j][k] == 1 && k <= (Container.TOTAL_LAYERS_FREEZE - 2)) {
                                if (cont[i][j][k + 1] == 0) {
                                    cont[i][j][k + 1] = 2;
                                }
                            }
                        }

                /**
                 * step 5
                 * 根据尺寸进行合并 （大箱）
                 */
                if (ContaienrSize == 1) {
                    for (int i = 0; i < Container.TOTAL_ROWS_FREEZE; i++)
                        for (int j = 0; j < Container.TOTAL_COLUMNS_FREEZE; j++)
                            for (int k = 0; k < Container.TOTAL_LAYERS_FREEZE; k++) {
                                if (cont[i][j][k] == 2 && j <= (Container.TOTAL_COLUMNS_FREEZE - 2)) {
                                    if (cont[i][j + 1][k] == 2) {
                                        //cont[i][j][k] = 2;
                                        //  cont[i][j+1][k] = 1; //相当于进行了插入
                                    } else {
                                        cont[i][j][k] = 0;
                                    }
                                } else if (cont[i][j][k] == 2 && j == (Container.TOTAL_COLUMNS_FREEZE - 1)) {
                                    cont[i][j][k] = 0;//处理边缘数据
                                }
                            }

                }
                /**
                 * step 6
                 * 遍历可插入的箱子位置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_FREEZE; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_FREEZE; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_FREEZE; k++) {
                            if (cont[i][j][k] == 2) {
                                Container container1 = new Container();
                                container1.setRow((byte) (i + Container.LOWER_LIMIT_FREEZE));
                                container1.setColumn((byte) (j + 1));
                                container1.setLayer((byte) (k + 1));
                                result.add(container1);
                            }
                        }

                return result;
            } else {
                //危险
                int cont[][][] = new int[Container.TOTAL_ROWS_HAZARD][Container.TOTAL_COLUMNS_HAZARD][Container.TOTAL_LAYERS_HAZARD];
                for (int i = 0; i < Container.TOTAL_ROWS_HAZARD; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_HAZARD; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_HAZARD; k++)
                            cont[i][j][k] = 0;
                /**
                 * step 1
                 * 获取所有空箱区的箱子的行列层数以及箱子尺寸加入到containerTools链表中
                 */
                for (Container container3 : Containers
                        ) {
                    Byte row = container3.getRow();
                    Byte column = container3.getColumn();
                    Byte layer = container3.getLayer();
                    Byte size = container3.getSize();
                    if (row >= Container.LOWER_LIMIT_HAZARD && row <= Container.UPPER_LIMIT_HAZARD) {
                        ContainerTool containerTool = new ContainerTool();
                        containerTool.setColumn(column);
                        containerTool.setLayer(layer);
                        containerTool.setRow((byte) (row - Container.UPPER_LIMIT_FREEZE));
                        containerTool.setSize(size);
                        containerTools.add(containerTool);
                    }
                }

                /**
                 * step 2
                 * 标记所有箱子的位置 设置为1
                 * 对大箱进行拆分 -- 相当于拆分成两个箱子
                 */
                for (ContainerTool containertool : containerTools
                        ) {
                    Byte row = containertool.getRow();
                    Byte column = containertool.getColumn();
                    Byte layer = containertool.getLayer();
                    Byte size = containertool.getSize();
                    if (size == 0)
                        cont[row - 1][column - 1][layer - 1] = 1;
                    else if (size == 1) {
                        cont[row - 1][column - 1][layer - 1] = 1;
                        cont[row - 1][column][layer - 1] = 1;
                    }
                }
                /**
                 * step 3
                 * 第一层可放箱子的区域设置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_HAZARD; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_HAZARD; j++) {
                        if (cont[i][j][0] == 0) {
                            cont[i][j][0] = 2;
                        }
                    }
                /**
                 * step 4
                 * 高层可放箱子的区域设置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_HAZARD; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_HAZARD; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_HAZARD; k++) {
                            if (cont[i][j][k] == 1 && k <= (Container.TOTAL_LAYERS_HAZARD - 2)) {
                                if (cont[i][j][k + 1] == 0) {
                                    cont[i][j][k + 1] = 2;
                                }
                            }
                        }

                /**
                 * step 5
                 * 根据尺寸进行合并 （大箱）
                 */
                if (ContaienrSize == 1) {
                    for (int i = 0; i < Container.TOTAL_ROWS_HAZARD; i++)
                        for (int j = 0; j < Container.TOTAL_COLUMNS_HAZARD; j++)
                            for (int k = 0; k < Container.TOTAL_LAYERS_HAZARD; k++) {
                                if (cont[i][j][k] == 2 && j <= (Container.TOTAL_COLUMNS_HAZARD - 2)) {
                                    if (cont[i][j + 1][k] == 2) {
                                        //cont[i][j][k] = 2;
                                        //  cont[i][j+1][k] = 1; //相当于进行了插入
                                    } else {
                                        cont[i][j][k] = 0;
                                    }
                                } else if (cont[i][j][k] == 2 && j == (Container.TOTAL_COLUMNS_HAZARD - 1)) {
                                    cont[i][j][k] = 0;//处理边缘数据
                                }
                            }

                }
                /**
                 * step 6
                 * 遍历可插入的箱子位置
                 */
                for (int i = 0; i < Container.TOTAL_ROWS_HAZARD; i++)
                    for (int j = 0; j < Container.TOTAL_COLUMNS_HAZARD; j++)
                        for (int k = 0; k < Container.TOTAL_LAYERS_HAZARD; k++) {
                            if (cont[i][j][k] == 2) {
                                Container container1 = new Container();
                                container1.setRow((byte) (i + Container.LOWER_LIMIT_HAZARD));
                                container1.setColumn((byte) (j + 1));
                                container1.setLayer((byte) (k + 1));
                                result.add(container1);
                            }
                        }

                return result;
            }
        }

    }
}

