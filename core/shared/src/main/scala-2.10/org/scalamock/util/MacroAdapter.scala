package org.scalamock.util

import scala.language.implicitConversions

class MacroAdapter[C <: MacroAdapter.Context](val ctx2: C) {
  import ctx2.universe._

  val noSelfType = ValDef(Modifiers(Flag.PRIVATE), nme.WILDCARD, TypeTree(NoType), EmptyTree)
  val termNames = nme
  val typeNames = tpnme

  def TermName(s: String) = newTermName(s)
  def TypeName(s: String) = newTypeName(s)
  def freshTerm(prefix: String): TermName = TermName(ctx2.fresh(prefix))

  def internalTypeRef(pre: Type, sym: Symbol, args: List[Type]) = TypeRef(pre, sym, args)
  def internalSuperType(thistpe: Type, supertpe: Type): Type = SuperType(thistpe, supertpe)
  def internalThisType(thistpe: Symbol) = ThisType(thistpe)
  def internalTypeDef(p: Symbol): TypeDef = TypeDef(p)

  implicit class TypeExtenders(t: Type) {
    def typeParams:List[Symbol] = {
      t match  {
        case PolyType(res, _) => res
        case _ => List.empty
      }
    }

    def typeArgs: List[Type] = {
      t match {
        case TypeRef(typ, tsym, typeParams) ⇒
          typeParams

        case _ ⇒
          List.empty
      }
    }

    def dealias:Type = t.normalize
    def resultType: Type = t
  }

  implicit class MethodExtenders(m: MethodSymbolApi) {
    def paramLists = m.paramss
  }
}

object MacroAdapter {
  type Context = scala.reflect.macros.Context
}