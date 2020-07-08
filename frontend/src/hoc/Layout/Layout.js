import React, { Component, Fragment } from "react";

import './Layout.css';
import Toolbar from '../../components/Navigation/Toolbar/Toolbar';
import Footer from '../../components/Navigation/Footer/Footer';

class Layout extends Component {
    render () {
        return (
            <Fragment>
                <Toolbar/>
                <main styleName='content'>
                    {this.props.children}
                </main>
                <Footer/>
            </Fragment>
        );
    }
}

export default Layout;
