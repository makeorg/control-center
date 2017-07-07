package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import scala.scalajs.js

object CustomRoutes {

  def customRoutes: js.Array[ReactElement] =
    js.Array(
        <.Route(^.exact := true, ^.path := "/", ^.component := Home())(),
        <.Route(^.exact := true, ^.path := "/propositions", ^.component := ProposalList())()
      )

}

//import React, { Component } from 'react';
//import { Switch, Route } from 'react-router-dom';
//import ListingPropositionsContainer from '../containers/Proposition/ListingPropositionsContainer';
//import ShowPropositionContainer from '../containers/Proposition/ShowPropositionContainer';
//
//import Home from './Home';
//
//class Main extends Component {
//  render() {
//    return (
//      <main>
//        <Switch>
//          <Route exact path='/propositions' component={ListingPropositionsContainer}/>
//          <Route path='/propositions/:id' component={ShowPropositionContainer}/>
//          <Route path='/' component={Home}/>
//        </Switch>
//      </main>
//      );
//  }
//}
