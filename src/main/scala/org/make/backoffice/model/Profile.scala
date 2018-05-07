package org.make.backoffice.model

import java.time.LocalDate

import scala.scalajs.js
import js.JSConverters._
import org.make.backoffice.util.JSConverters._

import scala.scalajs.js.UndefOr

@js.native
sealed trait Gender extends js.Object {
  def shortName: String
}

object Gender {
  def apply(shortName: String): Gender = js.Dynamic.literal(shortName = shortName).asInstanceOf[Gender]

  val male = Gender("M")
  val female = Gender("F")
  val other = Gender("O")

  val genders: Map[String, Gender] = Map(male.shortName -> male, female.shortName -> female, other.shortName -> other)

  def matchGender(gender: String): Option[Gender] = {
    val maybeGender = genders.get(gender)
    maybeGender
  }

}

@js.native
trait Profile extends js.Object {
  val dateOfBirth: js.UndefOr[LocalDate]
  val avatarUrl: js.UndefOr[String]
  val profession: js.UndefOr[String]
  val phoneNumber: js.UndefOr[String]
  val twitterId: js.UndefOr[String]
  val facebookId: js.UndefOr[String]
  val googleId: js.UndefOr[String]
  val gender: js.UndefOr[Gender]
  val genderName: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val karmaLevel: js.UndefOr[Int]
  val locale: js.UndefOr[String]
  val optInNewsletter: Boolean
  val age: UndefOr[Int]
}

object Profile {

  def getAge(dateOfBirth: Option[LocalDate]): Option[Int] = {
    dateOfBirth.map(d => LocalDate.now().getYear - d.getYear)
  }

  def apply(dateOfBirth: Option[LocalDate],
            avatarUrl: Option[String],
            profession: Option[String],
            phoneNumber: Option[String],
            twitterId: Option[String],
            facebookId: Option[String],
            googleId: Option[String],
            gender: Option[Gender],
            genderName: Option[String],
            postalCode: Option[String],
            karmaLevel: Option[Int],
            locale: Option[String],
            optInNewsletter: Boolean = false): Profile =
    js.Dynamic
      .literal(
        dateOfBirth = dateOfBirth.map(date => date.toJSDate).orUndefined,
        avatarUrl = avatarUrl.orUndefined,
        profession = profession.orUndefined,
        phoneNumber = phoneNumber.orUndefined,
        twitterId = twitterId.orUndefined,
        facebookId = facebookId.orUndefined,
        googleId = googleId.orUndefined,
        gender = gender.orUndefined,
        genderName = genderName.orUndefined,
        postalCode = postalCode.orUndefined,
        karmaLevel = karmaLevel.orUndefined,
        locale = locale.orUndefined,
        optInNewsletter = optInNewsletter,
        age = getAge(dateOfBirth).orUndefined
      )
      .asInstanceOf[Profile]

  def isEmpty(profile: Profile): Boolean =
    profile.dateOfBirth.isEmpty && profile.avatarUrl.isEmpty && profile.profession.isEmpty &&
      profile.phoneNumber.isEmpty && profile.twitterId.isEmpty && profile.facebookId.isEmpty &&
      profile.googleId.isEmpty && profile.gender.isEmpty && profile.genderName.isEmpty &&
      profile.postalCode.isEmpty && profile.karmaLevel.isEmpty && profile.locale.isEmpty &&
      !profile.optInNewsletter

  def parseProfile(dateOfBirth: Option[LocalDate] = None,
                   avatarUrl: Option[String] = None,
                   profession: Option[String] = None,
                   phoneNumber: Option[String] = None,
                   twitterId: Option[String] = None,
                   facebookId: Option[String] = None,
                   googleId: Option[String] = None,
                   gender: Option[Gender] = None,
                   genderName: Option[String] = None,
                   postalCode: Option[String] = None,
                   karmaLevel: Option[Int] = None,
                   locale: Option[String] = None,
                   optInNewsletter: Boolean = false): Option[Profile] = {

    val profile = Profile(
      dateOfBirth = dateOfBirth,
      avatarUrl = avatarUrl,
      profession = profession,
      phoneNumber = phoneNumber,
      twitterId = twitterId,
      facebookId = facebookId,
      googleId = googleId,
      gender = gender,
      genderName = genderName,
      postalCode = postalCode,
      karmaLevel = karmaLevel,
      locale = locale,
      optInNewsletter = optInNewsletter
    )
    if (isEmpty(profile)) {
      None
    } else {
      Some(profile)
    }
  }
}
