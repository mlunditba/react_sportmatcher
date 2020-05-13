import React, { Component, Fragment } from "react";

import Toolbar from '../../components/Navigation/Toolbar/Toolbar';

class Layout extends Component {
    render () {
        return (
            <Fragment>
                <Toolbar />
                <main>
                    {this.props.children}
                </main>
            </Fragment>
        );
    }
}

export default Layout;
