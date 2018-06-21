import React, { Component } from 'react';
import { Button } from 'antd';
import ReactEcharts from 'echarts-for-react';


class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      data: [
        {
          "name": "flare",
          "children": [
            {
              "name": "妈妈",
              "children": [
                { "name": "AgglomerativeCluster", "value": 3938 },
                { "name": "CommunityStructure", "value": 3812 },
                { "name": "HierarchicalCluster", "value": 6714 },
                { "name": "MergeEdge", "value": 743 }
              ]
            }
          ]
        }
      ]
    }
    this.getOption = this.getOption.bind(this);
  }

  getOption() {
    var option = {
      tooltip: {
        trigger: 'item',
        triggerOn: 'mousemove'
      },
      series: [
        {
          type: 'tree',

          data: this.state.data,

          left: '2%',
          right: '2%',
          top: '8%',
          bottom: '20%',

          symbol: 'emptyCircle',

          orient: 'vertical',

          // expandAndCollapse: true,

          label: {
            normal: {
              position: 'top',
              rotate: -90,
              verticalAlign: 'middle',
              align: 'right',
              fontSize: 9
            }
          },

          leaves: {
            label: {
              normal: {
                position: 'bottom',
                rotate: -90,
                verticalAlign: 'middle',
                align: 'left'
              }
            }
          },

          animationDurationUpdate: 750
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

export default App;
