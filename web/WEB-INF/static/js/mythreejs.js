var startThreejs;
var container_add;
var container_update;
var container_remove;
var container_shift;
var showThreejs;
var threejs_areas = [{
    lengthSeq: 10,
    widthSeq: 5,
    areaName: 'EMPTY'
}, {
    lengthSeq: 20,
    widthSeq: 10,
    areaName: 'ORDINARY'

}, {
    lengthSeq: 10,
    widthSeq: 6,
    areaName: 'FRIDGE'
}, {
    lengthSeq: 10,
    widthSeq: 6,
    areaName: 'HAZARD'
}];
(function () {
    var renderer;

    function initThree() {
        width = document.getElementById('canvas-frame').clientWidth;
        height = document.getElementById('canvas-frame').clientHeight;
        renderer = new THREE.WebGLRenderer({
            antialias: true
        });
        renderer.setSize(width, height);
        document.getElementById('canvas-frame').appendChild(renderer.domElement);
        renderer.setClearColor(0xFFFFFF, 1.0);
    }

    var camera;

    function initCamera() {
        camera = new THREE.PerspectiveCamera(45, width / height, 1, 2000);
        camera.position.x = 50;
        camera.position.y = 60;
        camera.position.z = 100;
        camera.lookAt({
            x: 0,
            y: 0,
            z: 0
        });
//        camera.up.x = 0;//相机以哪个方向为上方
//        camera.up.y = 0;
//        camera.up.z = 1;
    }

    var scene;

    function initScene() {
        scene = new THREE.Scene();
        var axis = new THREE.AxisHelper(20);
// 在场景中添加坐标轴
        scene.add(axis);
    }

    var light;

    function initLight() {
        light = new THREE.DirectionalLight(0xffffff, 1.5, 0);
        light.position.set(100, 200, 200);
        scene.add(light);
    }

    function animate() {
        //更新控制器
        controls.update();
        render();
        //更新性能插件
        requestAnimationFrame(animate);
    }

    function render() {
        renderer.render(scene, camera);
    }

    function initControls() {

        controls = new THREE.OrbitControls(camera, renderer.domElement);

        // 如果使用animate方法时，将此函数删除
        //controls.addEventListener( 'change', render );
        // 使动画循环使用时阻尼或自转 意思是否有惯性
        controls.enableDamping = true;
        //动态阻尼系数 就是鼠标拖拽旋转灵敏度
        controls.dampingFactor = 1;
        //是否可以缩放
        controls.enableZoom = true;
        //是否自动旋转
        //  controls.autoRotate = true;
        //设置相机距离原点的最远距离
        controls.minDistance = 50;
        //设置相机距离原点的最远距离
        controls.maxDistance = 600;
        //是否开启右键拖拽
        controls.enablePan = true;
    }


    function initTexture(url) {
        var texture = THREE.ImageUtils.loadTexture(url, {}, function () {
            //renderer.render(scene, camera);
        });
        return texture;
    }

    var unitLen = 10;
    var ground;
    var wallthick = 2;
    var groundthick = 2;
    var material;

    function initGround(lengthSeq, widthSeq) {
        var texture = initTexture('/static/img/brick_ground.jpg');
        var geo = new THREE.BoxGeometry(lengthSeq * unitLen, widthSeq * unitLen, groundthick);
        texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
        texture.repeat.set(lengthSeq, widthSeq);
        material = new THREE.MeshLambertMaterial({
            map: texture
        });
        material.side = 2;
        ground = new THREE.Mesh(geo, material);
        ground.rotation.x = -90 * Math.PI / 180;
        group.add(ground);

    }

    var backWall, leftWall;

    function initWall(lengthSeq, widthSeq, height) {

        var texture1 = initTexture('/static/img/brick.jpg');
        var texture = initTexture('/static/img/brick.jpg');
        texture.wrapS = texture.wrapT = texture1.wrapS = texture1.wrapT = THREE.RepeatWrapping;
        texture.repeat.set(lengthSeq, 2);
        texture1.repeat.set(widthSeq, 2);
        var material = new THREE.MeshLambertMaterial({
            map: texture
        });
        var material1 = new THREE.MeshLambertMaterial({
            map: texture1
        });
        var backgeo = new THREE.BoxGeometry(lengthSeq * unitLen, height, wallthick);
        var leftgeo = new THREE.BoxGeometry(widthSeq * unitLen + wallthick, height, wallthick);
        backWall = new THREE.Mesh(backgeo, material);
        leftWall = new THREE.Mesh(leftgeo, material1);
        backWall.position.z -= widthSeq * unitLen / 2 + wallthick / 2;
        leftWall.position.y = backWall.position.y = height / 2 - groundthick / 2 + backWall.position.y;
        leftWall.position.x -= lengthSeq * unitLen / 2 + wallthick / 2;
        leftWall.rotation.y = -90 * Math.PI / 180;
        leftWall.position.z -= wallthick / 2;
        group.add(backWall);
        group.add(leftWall);

    }


    function initName(name, lengthSeq, height) {

        var loader = new THREE.FontLoader();
        //注意是异步加载！
        loader.load('https://threejs.org/examples/fonts/optimer_bold.typeface.json', function (font) {
            var nameGeo = new THREE.TextGeometry(name, {
                // 设定文字字体，
                font: font,
                //尺寸
                size: 12,
                //厚度
                height: 2
            });
            var nameMesh = new THREE.Mesh(nameGeo);
            //   nameMesh.position.z -= lengthSeq * unitLen / 2 + wallthick;
            nameMesh.position.y += 10;
            nameMesh.position.x -= name.length * 5;
            backWall.add(nameMesh);
        });
    }

    var containerGeometrys = [], containerHeight, materialsList = [];
    var myContainers = [];

    function initContainer(length, width, height) {
        containerHeight = height;
        containerGeometrys.push(new THREE.BoxGeometry(length, height, width));
        containerGeometrys.push(new THREE.BoxGeometry(length * 2, height, width));
        for (var j = 1; j <= 6; j++) {
            var materials = [], front;
            front = j % 2 == 0 ? 4 : 3;
            var src = ['/static/img/ordinary_', '/static/img/fridge_', 'static/img/hazard_'];
            for (var i = 1; i <= 6; ++i) {
                var I = i > 2 ? front : i;
                var loadurl = src[Math.floor((j - 1) / 2)] + I + '.jpg';
                materials.push(new THREE.MeshBasicMaterial({
                    map: THREE.ImageUtils.loadTexture(loadurl),
                    overdraw: true
                }));
            }
            materialsList.push(materials);
        }

    }

    var group = new THREE.Group();

    container_add = function addAContainer(row, column, layer, type, size) {
        row = row || 1;
        column = column || 1;
        layer = layer || 1;
        type = type || 0;
        size = size || 0;
        var container = new THREE.Mesh(containerGeometrys[size], new THREE.MeshFaceMaterial(materialsList[type * 2 + size]));
        myContainers.push({
            containerObj: container,
            pos: {
                row: row,
                column: column,
                layer: layer
            }
        });
        container.position.z += unitLen / 2;
        group.add(container);
        //container.material.wireframe =true;
        container.position.y += groundthick / 2 + containerHeight / 2;
        container.position.z += (widthSeq / 2 - row) * unitLen;
        container.position.x -= (lengthSeq / 2 - column) * unitLen - unitLen / 2 * (size - 1);
        container.position.y += containerHeight * (layer - 1);


//            renderer.render(scene, camera);
    }
    container_remove = function (row, column, layer) {
        var name = 'row'+row+'column'+column+'layer'+layer;
        alert(name);
        // for (var x in myContainers) {
        //     if (myContainers[x].pos.row == row && myContainers[x].pos.column == column && myContainers[x].pos.layer == layer) {
        //         group.getObjectByName()
        //     }
        // }
    }

    function initGroup(lengthSeq, widthSeq, height, name) {
        lengthSeq = lengthSeq || 10;
        widthSeq = widthSeq || 10;
        height = height || 10;
        name = name || 'EMPTY';
        initGround(lengthSeq, widthSeq);
        initWall(lengthSeq, widthSeq, height);
        initName(name, lengthSeq, height);
        initContainer(10, 6, 6);
        scene.add(group);
    }

    var lengthSeq, widthSeq;
    startThreejs = function threeStart(type) {
        type = type || 0;
        lengthSeq = threejs_areas[type].lengthSeq;
        widthSeq = threejs_areas[type].widthSeq;
        var height = 20;
        var name = threejs_areas[type].areaName;
        initThree();
        initCamera();
        initScene();
        initLight();
        initGroup(lengthSeq, widthSeq, height, name);
        initControls();
        animate();
    }
    showThreejs = function (container) {
        // alert(container[0].id);
        for (var x in container) {
            container_add(container[x].row, container[x].column, container[x].layer, container[x].type, container[x].size);
        }
    }

})();


