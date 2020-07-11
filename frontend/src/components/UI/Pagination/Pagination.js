import React from 'react';

import './Pagination.css';
import {Link, withRouter} from "react-router-dom";

const pagination = (props) => (
    <div styleName='container'>
        <p>Showing {props.pageInitIndex}-{props.pageInitIndex + props.pageItemCount - 1} of {props.totalItemCount} entries</p>
        {props.totalPageCount !== 1 ? <div styleName='pageItems'>
            <Link styleName={'pageItem' + (props.pageNum === 1 ? ' disabled' : '')}
                to={props.match.url + '?pageNum=1'}
                exact>
                    <button onClick={() => props.onFetchMatches(1)} disabled={props.pageNum === 1}>First</button>
            </Link>
            { props.pageNum > 2 ?
                <Link styleName='pageItem'
                    to={props.match.url + '?pageNum=' + (props.pageNum - 2)}
                    exact>
                        <button onClick={() => props.onFetchMatches(props.pageNum - 2)}>{props.pageNum - 2}</button>
                </Link> : null
            }
            { props.pageNum > 1 ?
                <Link styleName='pageItem'
                    to={props.match.url + '?pageNum=' + (props.pageNum - 1)}
                    exact>
                        <button onClick={() => props.onFetchMatches(props.pageNum - 1)}>{props.pageNum - 1}</button>
                </Link> : null
            }
            <Link styleName='pageItem active'
                to={props.match.url + '?pageNum=' + (props.pageNum)}
                exact>
                    <button onClick={() => props.onFetchMatches(props.pageNum)} styleName='active'>{props.pageNum}</button>
            </Link>
            { (props.totalPageCount - props.pageNum) >= 1 ?
                <Link styleName='pageItem'
                    to={props.match.url + '?pageNum=' + (props.pageNum + 1)}
                    exact>
                        <button onClick={() => props.onFetchMatches(props.pageNum + 1)}>{props.pageNum + 1}</button>
                </Link> : null
            }
            { (props.totalPageCount - props.pageNum) >= 2 ?
                <Link styleName='pageItem'
                    to={props.match.url + '?pageNum=' + (props.pageNum + 2)}
                    exact>
                        <button onClick={() => props.onFetchMatches(props.pageNum + 2)}>{props.pageNum + 2}</button>
                </Link> : null
            }
            <Link styleName={'pageItem' + (props.pageNum === props.totalPageCount ? ' disabled' : '')}
                  to={props.match.url + '?pageNum=' + props.totalPageCount}
                  exact>
                <button onClick={() => props.onFetchMatches(props.totalPageCount)} disabled={props.pageNum === props.totalPageCount}>Last</button>
            </Link>
        </div> : null}
    </div>
);

export default withRouter(pagination);