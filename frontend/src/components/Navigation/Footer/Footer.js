import React from 'react';

import './Footer.css';

const footer = () => (
    <div styleName='footer'>
        <div styleName='container'>
            <div styleName='columns'>
                <div styleName='column'>
                    <h4>About</h4>
                    <ul>
                        <li>Press</li>
                        <li>Social Feed</li>
                        <li>Help</li>
                        <li>Contact Us</li>
                    </ul>
                </div>
                <div styleName='column'>
                    <h4>Events</h4>
                    <ul>
                        <li>Matches</li>
                        <li>Clubs</li>
                        <li>Tournaments</li>
                        <li>Leaderboard</li>
                    </ul>
                </div>
                <div styleName='column'>
                    <h4>Users</h4>
                    <ul>
                        <li>Profile</li>
                        <li>Find Friends</li>
                        <li>History</li>
                        <li>Report</li>
                    </ul>
                </div>
                <div styleName='column'>
                    <h4>Follow Us</h4>
                    <ul>
                        <li>Instagram</li>
                        <li>Facebook</li>
                        <li>YouTube</li>
                        <li>Twitter</li>
                    </ul>
                </div>
            </div>
            <div styleName='end-row'>
                <ul>
                    <li>© 2020 SportMatcher, Inc.</li>
                    <li>Privacy Policy</li>
                    <li>Terms</li>
                </ul>
            </div>
        </div>
    </div>
);

export default footer;