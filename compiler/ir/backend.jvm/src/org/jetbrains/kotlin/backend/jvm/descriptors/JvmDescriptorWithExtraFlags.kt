/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.backend.jvm.descriptors

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.PropertyDescriptorImpl
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.KotlinType

interface JvmDescriptorWithExtraFlags {
    val extraFlags: Int
}

class JvmPropertyDescriptorImpl(
        containingDeclaration: DeclarationDescriptor,
        original: PropertyDescriptor?,
        annotations: Annotations,
        modality: Modality,
        visibility: Visibility,
        override val extraFlags: Int,
        isVar: Boolean,
        name: Name,
        kind: CallableMemberDescriptor.Kind,
        source: SourceElement,
        isLateInit: Boolean,
        isConst: Boolean
) : JvmDescriptorWithExtraFlags, PropertyDescriptorImpl(
        containingDeclaration, original, annotations, modality, visibility, isVar,
        name, kind, source, isLateInit, isConst
) {
    override fun createSubstitutedCopy(
            newOwner: DeclarationDescriptor,
            newModality: Modality,
            newVisibility: Visibility,
            original: PropertyDescriptor?,
            kind: CallableMemberDescriptor.Kind
    ): PropertyDescriptorImpl =
            JvmPropertyDescriptorImpl(
                    newOwner, original, annotations, newModality, newVisibility, extraFlags, isVar, name, kind,
                    SourceElement.NO_SOURCE, isLateInit, isConst
            )

    companion object {
        fun createStaticVal(
                name: Name,
                type: KotlinType,
                containingDeclaration: DeclarationDescriptor,
                annotations: Annotations,
                modality: Modality,
                visibility: Visibility,
                extraFlags: Int,
                source: SourceElement
        ): PropertyDescriptorImpl =
                JvmPropertyDescriptorImpl(
                        containingDeclaration, null, annotations, modality, visibility, extraFlags, false, name,
                        CallableMemberDescriptor.Kind.SYNTHESIZED, source, false, false
                ).setTypeNoReceivers(type)

        private fun PropertyDescriptorImpl.setTypeNoReceivers(type: KotlinType): PropertyDescriptorImpl {
            setType(type, emptyList(), null as ReceiverParameterDescriptor?, null as ReceiverParameterDescriptor?)
            return this
        }
    }
}