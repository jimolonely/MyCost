import React, { Component } from 'react';
import {
    Button, Modal, Divider, Icon, Upload, Rate,
    Form, Select, Input, DatePicker, Radio,
} from 'antd';
import ReactEcharts from 'echarts-for-react';


const FormItem = Form.Item;
const Option = Select.Option;
const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

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
                    target: "呵呵",
                    label: {
                        show: true,
                        formatter: '朋友'
                    }
                }
            ],
            nodeVisible: false,
            updateInfoVisible: false,
        }
        this.getOption = this.getOption.bind(this);
        this.onChartClick = this.onChartClick.bind(this);
        this.handleNodeCancel = this.handleNodeCancel.bind(this);
        this.handleUpdateInfoCancel = this.handleUpdateInfoCancel.bind(this);
        this.onAddFamily = this.onAddFamily.bind(this);
        this.onSaveInfo = this.onSaveInfo.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
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
    handleUpdateInfoCancel() {
        this.setState({
            updateInfoVisible: false
        })
    }

    onAddFamily(e) {
        var type = e.target.id;
        console.log(type)
        this.setState({
            updateInfoVisible: true
        })
        if (type === "mother") {
        } else if (type === "father") {

        }
    }

    //更新或新增个人信息
    onSaveInfo() {

    }

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                console.log('Received values of form: ', values);
            }
        });
    }

    render() {
        let onEvents = {
            'click': this.onChartClick,
            // 'legendselectchanged': this.onChartLegendselectchanged
        };

        const { getFieldDecorator } = this.props.form;
        const formItemLayout = {
            labelCol: { span: 6 },
            wrapperCol: { span: 14 },
        };
        const datePickerConfig = {
            rules: [{ type: 'object', required: true, message: 'Please select time!' }],
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
                    <Button type="primary" onClick={this.onSaveInfo}><Icon type="check" />保存</Button>
                </Modal>

                <Modal
                    title="新增成员"
                    visible={this.state.updateInfoVisible}
                    footer={null}
                    onCancel={this.handleUpdateInfoCancel}
                >
                    <Divider orientation="left">基本信息</Divider>
                    <Form onSubmit={this.handleSubmit}>
                        <FormItem
                            {...formItemLayout}
                            label="姓名"
                        >
                            {getFieldDecorator('name', {
                                rules: [{ required: true, message: '名字必填!' }],
                            })(
                                <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Username" />
                            )}
                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label="性别"
                        >
                            {getFieldDecorator('sex', {
                                rules: [{ required: true, message: '性别必填!' }],
                            })(
                                <RadioGroup>
                                    <Radio value="男">男</Radio>
                                    <Radio value="女">女</Radio>
                                </RadioGroup>
                            )}
                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label="生日"
                        >
                            {getFieldDecorator('birthday', datePickerConfig)(
                                <DatePicker />
                            )}
                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label="血型"
                        >
                            {getFieldDecorator('blood', {
                                rules: [{ required: true, message: '血型必填!' }],
                            })(
                                <RadioGroup>
                                    <Radio value="O">O</Radio>
                                    <Radio value="A">A</Radio>
                                    <Radio value="B">B</Radio>
                                    <Radio value="AB">AB</Radio>
                                </RadioGroup>
                            )}
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label="文化程度"
                        >
                            {getFieldDecorator('education', {
                                rules: [{ required: true, message: '文化水平必填!' }],
                            })(
                                <RadioGroup>
                                    <Radio value="文盲">文盲</Radio>
                                    <Radio value="小学">小学</Radio>
                                    <Radio value="初中">初中</Radio>
                                    <Radio value="高中">高中</Radio>
                                    <Radio value="大学">大学</Radio>
                                    <Radio value="硕士">硕士</Radio>
                                    <Radio value="博士">博士</Radio>
                                </RadioGroup>
                            )}
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label="工作"
                        >
                            {getFieldDecorator('job', {
                                rules: [{ required: true, message: '工作必填!' }],
                            })(
                                <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Username" />
                            )}
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            label="备注"
                        >
                            {getFieldDecorator('remark', {
                                rules: [{ required: true, message: '必填!' }],
                            })(
                                <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Username" />
                            )}
                        </FormItem>


                        <FormItem
                            {...formItemLayout}
                            label="印象"
                        >
                            {getFieldDecorator('impression', {
                                rules: [
                                    { required: true, message: '大家对此人有何看法!', type: 'array' },
                                ],
                            })(
                                <Select mode="multiple" placeholder="大家对此人有何看法">
                                    <Option value="red">Red</Option>
                                    <Option value="green">Green</Option>
                                    <Option value="blue">Blue</Option>
                                </Select>
                            )}
                        </FormItem>


                        <FormItem
                            {...formItemLayout}
                            label="Upload"
                            extra="longgggggggggggggggggggggggggggggggggg"
                        >
                            {getFieldDecorator('upload', {
                                valuePropName: 'fileList',
                                getValueFromEvent: this.normFile,
                            })(
                                <Upload name="logo" action="/upload.do" listType="picture">
                                    <Button>
                                        <Icon type="upload" /> Click to upload
              </Button>
                                </Upload>
                            )}
                        </FormItem>
                        <FormItem
                            wrapperCol={{ span: 12, offset: 6 }}
                        >
                            <Button type="primary" htmlType="submit"><Icon type="check" />保存</Button>
                        </FormItem>
                    </Form>
                </Modal>
            </div>
        );
    }
}

const WrappedRelationShow = Form.create()(RelationShow);

export default WrappedRelationShow;
