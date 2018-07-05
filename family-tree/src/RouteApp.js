import React, { Component } from 'react';
import { Menu, Icon } from 'antd';
import {
    BrowserRouter as Router,
    Route, Link
} from 'react-router-dom';

import Login from './Login';
import RelationShow from './RelationShow';
import { relative } from 'upath';

const SubMenu = Menu.SubMenu;

class RouteApp extends Component {
    state = {
        current: 'home',
    }

    handleClick = (e) => {
        console.log('click ', e);
        this.setState({
            current: e.key,
        });
    }

    render() {
        return (
            <Router>
                <div>
                    <Menu
                        onClick={this.handleClick}
                        selectedKeys={[this.state.current]}
                        mode="horizontal"
                    >
                        <Menu.Item key='home'><Link to="/"><Icon type="home" />Home</Link></Menu.Item>
                        <Menu.Item key='map'><Link to="/map"><Icon type="user" />Map</Link></Menu.Item>

                    </Menu>
                    <Route exact path="/" component={Login} />
                    <Route exact path="/login" component={Login} />
                    <Route exact path="/map" component={RelationShow} />
                </div>
            </Router>
        )
    }
}

export default RouteApp;