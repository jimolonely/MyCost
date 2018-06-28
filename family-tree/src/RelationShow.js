import React, { Component } from 'react';
import { Button, Modal, Divider } from 'antd';
import ReactEcharts from 'echarts-for-react';


class RelationShow extends Component {

    constructor(props) {
        super(props);
        this.state = {
            graphHeight: 800,
            nodes: [
                {
                    name: "寂寞",
                    symbol: "image://http://img0.imgtn.bdimg.com/it/u=326486006,812505670&fm=27&gp=0.jpg",
                    symbolSize: 20
                },
                {
                    name: "呵呵",
                    symbol: "image://http://img0.imgtn.bdimg.com/it/u=326486006,812505670&fm=27&gp=0.jpg"
                    , symbolSize: 20
                }
            ],
            links: [
                {
                    source: "寂寞",
                    target: "呵呵"
                }
            ],
            nodeVisible: false
        }
        this.getOption = this.getOption.bind(this);
        this.onChartClick = this.onChartClick.bind(this);
        this.handleNodeCancel = this.handleNodeCancel.bind(this);
        this.onAddFamily = this.onAddFamily.bind(this);
    }

    getOption() {
        var option = {
            title: {
                text: '关系图',
                subtext: '数据来自寂寞',
                top: 'top',
                left: 'middle'
            },
            animationEasingUpdate: 'quinticInOut',
            tooltip: {
                trigger: 'item',
                formatter: '{a} : {b}'
            },
            toolbox: {
                show: true,
                feature: {
                    restore: { show: true },
                    magicType: { show: true, type: ['force', 'chord'] },
                    saveAsImage: { show: true }
                }
            },
            legend: {
                x: 'left',
                data: ['哈哈']
            },
            series: [
                {
                    type: 'graph',
                    name: "家族关系图",
                    layout: 'force',
                    edgeSymbol: ['arrow'],
                    categories: [
                        {
                            name: 'hehe'
                        }
                    ],
                    emphasis: {
                        lineStyle: {
                            width: 10
                        }
                    },
                    label: {
                        show: true,
                        position: 'inside',
                    },
                    lineStyle: {
                        normal: {
                            color: 'source',
                            curveness: 0.3
                        }
                    }
                    ,
                    roam: true,
                    nodes: this.state.nodes,
                    links: this.state.links
                }
            ]
        }
        return option;
    }

    //http://echarts.baidu.com/tutorial.html#ECharts%20%E4%B8%AD%E7%9A%84%E4%BA%8B%E4%BB%B6%E5%92%8C%E8%A1%8C%E4%B8%BA
    onChartClick(params) {
        console.log(params)
        if (params.componentType === 'series') {
            if (params.seriesType === 'graph') {
                var data = params.data;
                if (params.dataType === 'edge') {
                    // 点击到了 graph 的 edge（边）上。
                }
                else {
                    // 点击到了 graph 的 node（节点）上。
                    this.setState({
                        nodeVisible: true
                    })
                }
            }
        }
    }

    handleNodeCancel() {
        this.setState({
            nodeVisible: false
        })
    }

    onAddFamily(e) {
        var type = e.target.id;
        console.log(type)
        if (type === "mother") {

        } else if (type === "father") {

        }
    }

    render() {
        let onEvents = {
            'click': this.onChartClick,
            // 'legendselectchanged': this.onChartLegendselectchanged
        };

        return (
            <div>
                <Button type='primary'>计算2人关系</Button>
                <ReactEcharts option={this.getOption()} style={{ height: this.state.graphHeight + 'px' }}
                    onEvents={onEvents}
                />

                <Modal
                    title="节点操作"
                    visible={this.state.nodeVisible}
                    footer={null}
                    onCancel={this.handleNodeCancel}
                >
                    <Button type="default" onClick={this.onAddFamily} id="mother">添加母亲</Button>
                    <Button type="default" onClick={this.onAddFamily} id="father">添加父亲</Button>
                    <Button type="default" onClick={this.onAddFamily} id="son">添加儿子</Button>
                    <Button type="default" onClick={this.onAddFamily} id="daughter">添加女儿</Button>
                    <Button type="default" onClick={this.onAddFamily} id="love">添加配偶</Button>
                    <Divider orientation="left">基本信息</Divider>
                    
                </Modal>
            </div>
        );
    }
}

export default RelationShow;
