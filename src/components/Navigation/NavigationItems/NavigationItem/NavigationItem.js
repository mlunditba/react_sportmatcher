import React from 'react';
import { NavLink } from 'react-router-dom';

import './NavigationItem.css';

const navigationItem = (props) => (
    <li styleName='navigationItem'>
        <NavLink
            to={props.link}
            exact={props.exact}>{props.children}</NavLink>
    </li>
);

export default navigationItem;