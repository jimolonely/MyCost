import React, { Component } from 'react';
import { Button } from 'antd';
import ReactEcharts from 'echarts-for-react';


class RelationShow extends Component {

    constructor(props) {
        super(props);
        this.state = {
            nodes: [],
            links: []
        }
        this.getOption = this.getOption.bind(this);
    }

    getOption() {
        var option = {
            title: {
                text: '关系图',
                subtext: '数据来自寂寞',
                top: 'top',
                left: 'middle'
            },
            // animationDuration: 500,
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

    render() {
        return (
            <div>
                <Button type='primary'>click me</Button>
                <ReactEcharts option={this.getOption()} />
            </div>
        );
    }
}

export default RelationShow;
