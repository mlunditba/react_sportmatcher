import React from 'react';

import './NavigationItems.css';
import NavigationItem from './NavigationItem/NavigationItem';
import Button from '../../UI/Button/Button';

const navigationItems = () => (
    <div styleName='nav'>
        <ul styleName='navigationItems'>
            <NavigationItem link="/aboutus" exact>About us</NavigationItem>
            <NavigationItem link="/matches" exact>Matches</NavigationItem>
            <NavigationItem link="/clubs" exact>Clubs</NavigationItem>
            <NavigationItem link="/pitches" exact>Pitches</NavigationItem>
            <div styleName='accountBtns'>
                <div styleName='accountBtn'>
                    <Button btnType='informative'>Sign in</Button>
                </div>
                <div styleName='accountBtn'>
                    <Button btnType='informative'>Sign up</Button>
                </div>
            </div>
        </ul>
    </div>
);

export default navigationItems;