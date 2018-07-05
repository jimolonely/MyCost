import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
// import Login from './Login';
import RouteApp from './RouteApp';
// import RelationShow from './RelationShow';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(<RouteApp />, document.getElementById('root'));
registerServiceWorker();
