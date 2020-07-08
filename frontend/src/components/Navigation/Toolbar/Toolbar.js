import React, { Component } from 'react';

import './Toolbar.css';
import Logo from '../../Logo/Logo';
import NavigationItems from '../NavigationItems/NavigationItems';

class Toolbar extends Component {
    state = {
        isTop: true
    }

    componentDidMount() {
        window.addEventListener('scroll', this.scrollHandler);
    }

    scrollHandler = (event) => {
        const isTop = window.scrollY < window.innerHeight * 2 / 5;
        if (isTop !== this.state.isTop) {
            this.setState({ isTop })
        }
    }

    render () {
        return (
            <header styleName={['toolbar', this.state.isTop ? '' : 'collapse'].join(' ')}>
                <div styleName='logo'>
                    <Logo/>
                </div>
                <nav styleName={'desktopOnly'}>
                    <NavigationItems />
                </nav>
            </header>
        );
    }
}

export default Toolbar;