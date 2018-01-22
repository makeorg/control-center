package org.make.backoffice.components.aor.idea

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.facades.AdminOnRest.Datagrid._
import org.make.backoffice.facades.AdminOnRest.Edit._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.FormTab._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.ShowButton._
import org.make.backoffice.facades.AdminOnRest.TabbedForm._
import org.make.backoffice.facades.{Match, Params}
import org.make.backoffice.models.Idea
import org.make.client.Resource

object EditIdea {

  case class TitleState(idea: Idea)

  lazy val ideaTitle: ReactClass = React.createClass[Unit, TitleState](getInitialState = self => {
    val idea = self.props.native.record.asInstanceOf[Idea]
    TitleState(idea)
  }, componentWillUpdate = (self, props, _) => {
    val idea = props.native.record.asInstanceOf[Idea]
    self.setState(_.copy(idea))
  }, render = self => {
    <.h1()(self.state.idea.name)
  })

  case class EditProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React.createClass[EditProps, Unit](
      displayName = "EditIdea",
      render = self =>
        <.Edit(
          ^.resource := Resource.ideas,
          ^.location := self.props.location,
          ^.`match` := Match(params = Params(id = self.props.location.pathname.split('/')(2))), //todo: investigate a better way to get the id
          ^.editTitle := <.IdeaTitle()()
        )(
          <.TabbedForm()(
            <.FormTab(^.label := "Infos")(<.TextInput(^.source := "name", ^.options := Map("fullWidth" -> true))()),
            <.FormTab(^.label := "Proposals list")(
              <.ReferenceManyField(^.addLabel := false, ^.reference := Resource.proposals, ^.target := "ideaId")(
                <.Datagrid()(
                  <.ShowButton()(),
                  <.TextField(^.source := "content", ^.sortable := false)(),
                  <.ReferenceArrayField(
                    ^.label := "Tags",
                    ^.reference := Resource.tags,
                    ^.source := "tagIds",
                    ^.sortable := false
                  )(<.SingleFieldList()(<.ChipField(^.source := "label")())),
                  <.TextField(^.label := "Author", ^.source := "author.firstName", ^.sortable := false)()
                )
              )
            )
          )
      )
    )

}
