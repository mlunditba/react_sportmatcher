import React, { Component, Fragment } from "react";

import './Layout.css';
import Toolbar from '../../components/Navigation/Toolbar/Toolbar';

class Layout extends Component {
    render () {
        return (
            <Fragment>
                <Toolbar />
                <main styleName='content'>
                    {this.props.children}
                </main>
            </Fragment>
        );
    }
}

export default Layout;
