<template>
    <div v-if="!hasVerified" class="bg-white">
        <div class="mx-auto max-w-7xl py-24 sm:px-6 sm:py-32 lg:px-8">
            <div class="relative isolate overflow-hidden bg-gray-900 px-6 py-24 text-center shadow-2xl sm:rounded-3xl sm:px-16">
                <h2 class="mx-auto max-w-2xl text-3xl font-bold tracking-tight text-white sm:text-4xl">Verify your credential to
                    proceed</h2>
                <p class="mx-auto mt-6 max-w-xl text-lg leading-8 text-gray-300">To continue past this point, you are required to present a
                    valid identification Verifiable Credential.</p>
                <div class="mt-10 flex items-center justify-center gap-x-6">
                    <button class="rounded-md bg-white px-3.5 py-2.5 text-sm font-semibold text-gray-900 shadow-sm hover:bg-gray-100 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-white"
                            @click="verify"
                    >
                        Verify<span v-if="isPending">...</span>
                    </button>
                </div>
                <svg aria-hidden="true"
                     class="absolute left-1/2 top-1/2 -z-10 h-[64rem] w-[64rem] -translate-x-1/2 [mask-image:radial-gradient(closest-side,white,transparent)]"
                     viewBox="0 0 1024 1024"
                >
                    <circle cx="512" cy="512" fill="url(#827591b1-ce8c-4110-b064-7cb85a0b1217)" fill-opacity="0.7" r="512"/>
                    <defs>
                        <radialGradient id="827591b1-ce8c-4110-b064-7cb85a0b1217">
                            <stop stop-color="#7775D6"/>
                            <stop offset="1" stop-color="#E935C1"/>
                        </radialGradient>
                    </defs>
                </svg>
            </div>

            <div v-if="url && url != ''" class="mt-5 bg-white p-4">
                <div class="flex justify-center gap-4 items-center">
                    <div style="width: 300px">
                        <qrcode-vue :size="300" :value="url" level="H"/>
                    </div>
                    <p class="text-neutral-950 font-mono">{{ url }}</p>
                </div>
            </div>
        </div>
    </div>

    <div v-else class="bg-white py-24 sm:py-32">
        <ul>
            <li v-for="cred in receivedCredentials">
                <VerifiedCred :credential="cred"></VerifiedCred>
            </li>
        </ul>
    </div>
</template>

<script lang="ts" setup>
import QrcodeVue from "qrcode.vue";
import {type Ref, ref} from "vue";
import {useFetch} from "#app";

const isPolling = ref(false)
const hasVerified = ref(false)
const isPending = ref(false)
const url = ref("")
const nonce = ref("")

const backendHost = useRuntimeConfig().public.backendHost

const receivedCredentials: Ref<Object | null> = ref(null)

async function checkVerificationResult(): Promise<boolean> {
    const response = await fetch(`${backendHost}/entra/status/${nonce.value}`)

    if (response.status == 404) {
        return false
    } else {
        receivedCredentials.value = await response.json()
        hasVerified.value = true

        return true
    }


}

async function verify() {
    const reqBody = {
        "data": {
            "vc_policies": [
                "expired", "not-before",
                { policy: "allowed-issuer", args: "did:web:verifiedid.entra.microsoft.com:a8671fa1-780f-4af1-8341-cd431da2c46d:356de688-3752-d83c-6225-9ae1005e2aeb" }
                //{ policy: "allowed-issuer", args: "did:web:entra.walt.id" }
            ]
        },
        "entraVerification": {
            //"authority": "did:web:entra.walt.id",
            "authority": "did:web:verifiedid.entra.microsoft.com:a8671fa1-780f-4af1-8341-cd431da2c46d:356de688-3752-d83c-6225-9ae1005e2aeb",
            "credentials": [
                {
                   /* "acceptedIssuers": [
                        "did:web:entra.walt.id"
                    ],*/
                    "purpose": "TEST",
                    "type": "MyID"
                }
            ]
        }
    }

    const {data, pending, error, refresh} = await useFetch(`${backendHost}/entra/verify`, {
        method: "POST",
        body: reqBody,

        onRequest({request, options}) {
            isPending.value = true
        },
        onResponse({request, response, options}) {
            isPending.value = false
        },
        onResponseError({request, response, options}) {
            isPending.value = false
        }
    })

    nonce.value = data.value.nonce
    url.value = data.value.url

    isPolling.value = true
    const interval = setInterval(async () => {
        if (await checkVerificationResult()) {
            clearInterval(interval)
        }
    }, 1000)
}

</script>

<style scoped>

</style>
