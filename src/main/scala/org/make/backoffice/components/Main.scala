package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._

object Main {

  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) => <.main()(
      <.Switch()(
        <.Route(^.exact := true, ^.path := "/", ^.component := Home())(),
        <.Route(^.exact := true, ^.path := "/propositions", ^.component := ProposalList())(),
        <.Route(^.path := s"/propositions/:id", ^.component := ShowProposition())()
      )
    )
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
