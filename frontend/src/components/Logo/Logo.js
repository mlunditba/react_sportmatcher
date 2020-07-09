import React from 'react';

import './Logo.css';
import {NavLink} from "react-router-dom";
// import basketballLogo from '../../assets/images/logo.png';

const logo = () => (
    <div styleName='logo'>
        {/* <img src={basketballLogo} alt="Sport Matcher" /> */}
        <NavLink
            to="/" exact><h1>SportMatcher</h1></NavLink>
    </div>
);

export default logo;