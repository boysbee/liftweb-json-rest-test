package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._
import mapper._
import code.model._
import net.liftmodules.JQueryModule
import code.snippet._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {

  val admin_email = "admin@kuczak2.com"
  val admin_password = "adminkurczak2"

  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
            "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User)
    
    ///adding admin user if not present
    if (User.find(Like(User.email, admin_email)) == Empty) {
      println("Adding superuser")
      val user = User.create
      user.email.set(admin_email)
      user.superUser.set(true)
      user.firstName.set("admin")
      user.lastName.set("admin")
      user.password.set(admin_password)
      user.validated.set(true)
      user.save
    } else {
      println("Superuser account present")
    }

    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    // Build SiteMap
    def sitemap: SiteMap = {
      val sitemap: List[Menu] = List(
        Menu.i("Index") / "index" >> LocGroup("main"),
        User.loginMenuLoc.get,
        User.logoutMenuLoc.get
        )

      val fullMenu = sitemap
      SiteMap(fullMenu: _*)
    }

    def sitemapMutators = User.sitemapMutator

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    //LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))
    LiftRules.setSiteMapFunc(() => sitemap)

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery = JQueryModule.JQuery172
    JQueryModule.init()

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
    
    // adding json reply
    LiftRules.dispatch.append(JsonReply)
  }
}
